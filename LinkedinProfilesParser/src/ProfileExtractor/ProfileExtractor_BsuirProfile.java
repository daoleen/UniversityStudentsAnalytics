package ProfileExtractor;
import com.ibm.bigsheets.macros.textanalytics.TextAnalyticsMacro;
import org.apache.m2.model.formula.exception.FormulaException;
public class ProfileExtractor_BsuirProfile extends TextAnalyticsMacro {
  public ProfileExtractor_BsuirProfile() throws FormulaException {
    initialize("ProfileExtractor.BsuirProfile","LinkedinProfilesParser",false,new String[]{"ProfileExtractor"},"ProfileExtractor.BsuirProfile","MULTILINGUAL");
  }
}
