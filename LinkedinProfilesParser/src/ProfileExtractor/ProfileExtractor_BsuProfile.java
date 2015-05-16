package ProfileExtractor;
import com.ibm.bigsheets.macros.textanalytics.TextAnalyticsMacro;
import org.apache.m2.model.formula.exception.FormulaException;
public class ProfileExtractor_BsuProfile extends TextAnalyticsMacro {
  public ProfileExtractor_BsuProfile() throws FormulaException {
    initialize("ProfileExtractor.BsuProfile","LinkedinProfilesParser",false,new String[]{"ProfileExtractor"},"ProfileExtractor.BsuProfile","MULTILINGUAL");
  }
}
