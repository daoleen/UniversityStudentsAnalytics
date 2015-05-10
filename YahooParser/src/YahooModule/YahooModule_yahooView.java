package YahooModule;
import com.ibm.bigsheets.macros.textanalytics.TextAnalyticsMacro;
import org.apache.m2.model.formula.exception.FormulaException;
public class YahooModule_yahooView extends TextAnalyticsMacro {
  public YahooModule_yahooView() throws FormulaException {
    initialize("YahooModule.yahooView","YahooParser",false,new String[]{"YahooModule"},"YahooModule.yahooView","MULTILINGUAL");
  }
}
