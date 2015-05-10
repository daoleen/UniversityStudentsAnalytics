package ProfileExtractor;
import com.ibm.bigsheets.macros.textanalytics.TextAnalyticsMacro;
import org.apache.m2.model.formula.exception.FormulaException;
public class RussianNamesExtractor_RussianNamesView extends TextAnalyticsMacro {
  public RussianNamesExtractor_RussianNamesView() throws FormulaException {
    initialize("RussianNamesExtractor.RussianNamesView","BsuirProfilesParser",false,new String[]{"ProfileExtractor"},"RussianNamesExtractor.RussianNamesView","MULTILINGUAL");
  }
}
