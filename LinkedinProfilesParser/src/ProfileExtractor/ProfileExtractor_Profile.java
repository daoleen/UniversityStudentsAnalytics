package ProfileExtractor;
import com.ibm.bigsheets.macros.textanalytics.TextAnalyticsMacro;
import org.apache.m2.model.formula.exception.FormulaException;
public class ProfileExtractor_Profile extends TextAnalyticsMacro {
  public ProfileExtractor_Profile() throws FormulaException {
    initialize("ProfileExtractor.Profile2","Linkedin-Profiles-Parser",false,new String[]{"ProfileExtractor"},"ProfileExtractor.Profile","MULTILINGUAL");
  }
}
