package YahooModule;
import com.ibm.bigsheets.macros.textanalytics.TextAnalyticsMacro;
import org.apache.m2.model.formula.exception.FormulaException;
public class YahooModule_yahooUrlsView extends TextAnalyticsMacro {
  public YahooModule_yahooUrlsView() throws FormulaException {
    initialize("YahooModule.yahooUrlsView","YahooParser",false,new String[]{"YahooModule"},"YahooModule.yahooUrlsView","MULTILINGUAL");
  }
}
