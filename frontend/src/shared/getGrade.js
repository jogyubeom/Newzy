import level1 from "./images/pencil.svg";
import level2 from "./images/sharppen.svg";
import level3 from "./images/pen.svg";
import level4 from "./images/feather-pen.svg";

export function getGrade(num) {
  switch (num) {
    case 1:
      return level1;
    case 2:
      return level2;
    case 3:
      return level3;
    case 4:
      return level4;
    default:
      return null;
  }
}
