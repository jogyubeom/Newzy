// utils/dateFormatter.js

export const KoreanWeek = (date) => {
  const month = date.getMonth() + 1; // 월 (0부터 시작하므로 1을 더함)
  const day = date.getDate();

  // 해당 달의 첫 번째 날 구하기
  const firstDayOfMonth = new Date(date.getFullYear(), date.getMonth(), 1);
  const firstDayOfWeek = firstDayOfMonth.getDay() || 7; // 일요일(0)은 7로 처리

  // 현재 날짜 기준 몇째 주인지 계산
  const weekNumber = Math.ceil((day + firstDayOfWeek - 1) / 7);

  return `${month}월 ${weekNumber}째주의`;
};
