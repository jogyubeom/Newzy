import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import useAuthStore from '../../shared/store/userStore';
import TitleInput from './ui/titleInput';
import CategorySelector from './ui/categorySelector';
import ContentEditor from './ui/contentEditor';
import baseAxios from 'shared/utils/baseAxios';

export const NewzyEdit = () => {
  const navigate = useNavigate();
  const { newzyId } = useParams();
  const { userInfo, token } = useAuthStore(); // 토큰 가져오기
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    title: '',
    category: 0,
    content: '',
    nickname: ''
  });

  // 토큰이 없는 경우 로그인 페이지로 리다이렉트
  useEffect(() => {
    if (!token) {
      alert('로그인이 필요합니다.');
      navigate('/newzy');
    }
  }, [token, navigate]);

  useEffect(() => {
    window.scrollTo(0, 0);
    if (newzyId) {
      fetchNewzyData(newzyId);
      setIsEditing(true);
    }
  }, [newzyId]);

  // 기존 newzy 데이터를 서버에서 가져옴
  const fetchNewzyData = async (id) => {
    try {
      const response = await baseAxios().get(`/newzy/${id}`);
      if (response.status === 200) {
        const { title, category, content, nickname } = response.data;
        setFormData({ title, category, content, nickname });

        if (userInfo?.nickname !== nickname) {
          alert('본인의 게시물만 수정할 수 있습니다.');
          navigate('/newzy');
        }
      } else {
        alert('게시물을 찾을 수 없습니다.'); // 게시물이 존재하지 않을 때
        navigate('/newzy');
      }
    } catch (error) {
      console.error("Error fetching newzy data:", error);
      alert('데이터를 불러오는 데 실패했습니다.');
      navigate('/newzy'); // 오류 발생 시 목록 페이지로 이동
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleCategoryChange = (category) => {
    setFormData((prevData) => ({ ...prevData, category }));
  };

  const handleSave = async () => {
    if (!formData.title || formData.category === null || !formData.content) {
      alert('모든 내용을 채워주세요.');
      return;
    }

    try {
      if (isEditing) {
        // 수정 로직 (PATCH 요청)
        const response = await baseAxios().patch(`/newzy/${newzyId}`, formData);
        if (response.status === 200) {
          alert('수정되었습니다.');
          navigate('/newzy');
        } else {
          alert('수정에 실패했습니다.');
        }
      } else {
        // 새 글 저장 로직 (POST 요청)
        const response = await baseAxios().post('/newzy', formData);
        if (response.status === 201) {
          alert('저장되었습니다.');
          navigate('/newzy');
        } else {
          alert('저장에 실패했습니다.');
        }
      }
    } catch (error) {
      alert('오류가 발생했습니다.');
      console.error("Error saving data:", error);
    }
  };

  return (
    <div className="bg-white">
      <div className="w-full h-[50px] border-b border-gray-200 shadow relative">
        <button
          onClick={handleSave}
          className="w-[80px] h-[30px] bg-[#565656] text-white text-[20px] font-bold rounded-md absolute top-[10px] right-0 mr-4"
        >
          {isEditing ? '수정' : '저장'}
        </button>
      </div>

      <div className="flex h-screen">
        <div className="w-[15%]"></div>

        <div className="flex-1 p-6">
          <div className="mb-3">
            <TitleInput title={formData.title} onChange={handleChange} />
          </div>

          <div className="mb-8">
            <CategorySelector category={formData.category} onCategoryChange={handleCategoryChange} />
          </div>

          <ContentEditor content={formData.content} setContent={(content) => setFormData((prevData) => ({ ...prevData, content }))} />
        </div>

        <div className="w-[15%] relative"></div>
      </div>
    </div>
  );
};