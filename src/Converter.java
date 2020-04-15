import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nhom BTD on 14/04/2020
 */
public class Converter {
    private StringBuilder stringBuilder;
    private String imagePartern;
    
    public Converter(){
        stringBuilder = new StringBuilder();
    }

    public String convert(String string){
        boolean isImage = false;
        if (string == null || string.length() == 0)
            return "";
        string = string.replace("    *", "*");
        this.stringBuilder.delete(0, this.stringBuilder.length());

        char kiTu = string.charAt(0);

        for(int i = 0; i < string.length(); ++i) {
            if (isImage && string.charAt(i) == '\n') {
                this.stringBuilder.append("\"/>");
                isImage = false;
            }
            if (i != 0 && string.charAt(i - 1) != '\n') {
                this.stringBuilder.append(string.charAt(i));
            } else {
                if (i != 0) {
                    switch(kiTu) {
                        case '!':
                            this.stringBuilder.append("</b><br></font>");
                            break;
                        case '#':
                            this.stringBuilder.append("</b><br>");
                            break;
                        case '*':
                            this.stringBuilder.append("<br></b></font>");
                            break;
                        case '+':
                            this.stringBuilder.append("<br></font>");
                            break;
                        case '-':
                            this.stringBuilder.append("<br>");
                            break;
                        case '=':
                            this.stringBuilder.append("<br></font>");
                            break;
                        case '@':
                            this.stringBuilder.append("</b></font><br>");
                            break;
//                        case '~':
//                            this.stringBuilder.append("<br>");
//                            break;
                        default:
                            this.stringBuilder.append("<br>");
                    }
                }

                switch(string.charAt(i)) {
                    case '!':
                        this.stringBuilder.append("<font color=#C00000><b>!");
                        break;
                    case '#':
                        this.stringBuilder.append("<b>#");
                        break;
                    case '*':
                        this.stringBuilder.append("<font color=blue><b>*");
                        break;
                    case '+':
                        this.stringBuilder.append("<font color=gray>+");
                        break;
                    case '-':
                        this.stringBuilder.append("-");
                        break;
                    case '=':
                        this.stringBuilder.append("<font color=green>=");
                        break;
                    case '@':
                        this.stringBuilder.append("<font color=red><b>@");
                        break;
                    case '~':
                        this.stringBuilder.append("<img style=\"max-width: 100%;height: auto;\" src=\"[mspdict-path]/Images/");
                        isImage = true;
                        break;
                    default:
                        this.stringBuilder.append(string.charAt(i));
                }

                kiTu = string.charAt(i);
            }
        }

        switch(kiTu) {
            case '!':
                this.stringBuilder.append("</b><br></font>");
                break;
            case '*':
                this.stringBuilder.append("<br></b></font>");
                break;
            case '+':
                this.stringBuilder.append("<br></font>");
                break;
            case '-':
                this.stringBuilder.append("<br>");
                break;
            case '=':
                this.stringBuilder.append("<br></font>");
                break;
            case '@':
                this.stringBuilder.append("</b></font><br>");
                break;
//            case '~':
//                this.stringBuilder.append("\"/><br>");
//                break;
            default:
                this.stringBuilder.append("<br>");
        }

//        this.stringBuilder.append("</font>");
        return this.stringBuilder.toString();
    }
}
