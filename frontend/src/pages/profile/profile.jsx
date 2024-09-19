import Header from "../../widgets/Header";
import { CgProfile } from "react-icons/cg";

const Profile = () => {
  return (
    <div>
      <Header/>

      <div className=" w-full h-[409px] bg-[#132956] relative flex items-center px-8">

      <div>
        <CgProfile className="absolute top-[70px] left-[53px] w-[270px] h-[270px]"/>
        <div className="absolute top-[263px] left-[249px] w-[100px] h-[100px]">
            <svg id="21:11147" className="absolute top-0 left-0 w-[100px] h-[100px]"></svg><img src="https://image-resource.creatie.ai/136922663561920/136922663561929/73c7ca5f71159d463c3aa99e7ed0d328.png" className="absolute top-[20px] left-[20px] w-[60px] h-[60px] object-cover" />
        </div>
      </div>

      <div className=" ml-[350px]">
        <div className="h-[103px] text-white font-[Open Sans] text-[46px] leading-[24px] font-semibold flex items-center">
            정지훈
        </div>
        <div className="w-[190px] h-[96px] text-white font-[Open Sans] text-[24px] leading-[36px] font-semibold flex items-center">
            <div><span>안녕하세요.<div></div>잘 부탁드립니다!</span><span></span></div>
        </div>
      </div>

      <div className="flex flex-col items-center ml-auto">
        <div className="flex gap-20">
          <div className="flex flex-col items-center">
            <div className="w-[166px] h-[103px] text-white font-[Poppins] text-[36px] font-semibold flex items-center">
                Followers
            </div>
            <div className="w-[100px] h-[100px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
                42
            </div>
          </div>
          <div className="flex flex-col items-center">
            <div className="w-[123px] h-[103px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center">
                Newzy
            </div>
            <div className="w-[100px] h-[100px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
              7
            </div>
          </div>
          <div className="flex flex-col items-center">
            <div className="w-[188px] h-[103px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center">
                Followings
            </div>
            <div className="w-[100px] h-[100px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
              25
            </div>
          </div>
        </div>
        <div className="w-[293px] h-[103px]">
          <div className="w-[293px] h-[73px] rounded-[45px] bg-[#3578FF]">
          </div>
          <div className="h-[103px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center">
              프로필 편집
          </div>
        </div>
      </div>

      </div>
    </div>
  );
};

export default Profile;
