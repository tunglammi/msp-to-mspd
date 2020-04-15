import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by nhom on BTD 14/04/2020
 */
public class LangComparator implements Comparator{
    String s1;
    String s2;
    int i;
    int char1;
    int char2;
    int longer;
    int min;

    public int compare(Object emp1, Object emp2) {
        this.s1 = ((ThongTinTu)emp1).tu.toLowerCase();
        this.s2 = ((ThongTinTu)emp2).tu.toLowerCase();
        if (this.s1 == null) {
            this.s1 = "";
        }

        if (this.s2 == null) {
            this.s2 = "";
        }

        this.longer = this.s1.length() - this.s2.length();
        if (this.longer >= 0) {
            this.min = this.s2.length();
        } else {
            this.min = this.s1.length();
        }

        for(this.i = 0; this.i < this.min; ++this.i) {
            this.char1 = this.s1.charAt(this.i);
            this.char2 = this.s2.charAt(this.i);
            if (this.char1 > this.char2) {
                return 1;
            }

            if (this.char1 < this.char2) {
                return -1;
            }
        }

        if (this.longer > 0) {
            return 1;
        } else if (this.longer < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
