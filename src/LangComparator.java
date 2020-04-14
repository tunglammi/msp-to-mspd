import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class LangComparator implements Comparator{
    Collator collator;

    public LangComparator(String lang) {
        this.collator = Collator.getInstance(new Locale(lang));
    }

    public int compare(Object emp1, Object emp2) {
        return this.collator.compare(((ThongTinTu)emp1).tu, ((ThongTinTu)emp2).tu);
    }
}
