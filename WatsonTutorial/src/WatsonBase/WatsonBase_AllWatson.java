package WatsonBase;
import com.ibm.bigsheets.macros.textanalytics.TextAnalyticsMacro;
import org.apache.m2.model.formula.exception.FormulaException;
public class WatsonBase_AllWatson extends TextAnalyticsMacro {
  public WatsonBase_AllWatson() throws FormulaException {
    initialize("WatsonBase.AllWatson","WatsonTutorial",true,new String[]{"HighQKeywords","WatsonBase"},"WatsonBase.AllWatson","MULTILINGUAL");
  }
}
