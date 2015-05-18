package ProfileExtractor;
import com.ibm.bigsheets.macros.textanalytics.TextAnalyticsMacro;
import org.apache.m2.model.formula.exception.FormulaException;
public class ProfileExtractor_UniqueLocations extends TextAnalyticsMacro {
  public ProfileExtractor_UniqueLocations() throws FormulaException {
    initialize("ProfileExtractor.UniqueLocations","LinkedinProfilesParser",false,new String[]{"ProfileExtractor"},"ProfileExtractor.UniqueLocations","MULTILINGUAL");
  }
}
