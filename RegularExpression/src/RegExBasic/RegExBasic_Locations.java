package RegExBasic;
import com.ibm.bigsheets.macros.textanalytics.TextAnalyticsMacro;
import org.apache.m2.model.formula.exception.FormulaException;
public class RegExBasic_Locations extends TextAnalyticsMacro {
  public RegExBasic_Locations() throws FormulaException {
    initialize("RegExBasic.Locations","RegularExpression",false,new String[]{"RegExBasic"},"RegExBasic.Locations","MULTILINGUAL");
  }
}
