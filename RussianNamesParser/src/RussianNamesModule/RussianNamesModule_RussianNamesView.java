package RussianNamesModule;
import com.ibm.bigsheets.macros.textanalytics.TextAnalyticsMacro;
import org.apache.m2.model.formula.exception.FormulaException;
public class RussianNamesModule_RussianNamesView extends TextAnalyticsMacro {
  public RussianNamesModule_RussianNamesView() throws FormulaException {
    initialize("RussianNamesModule.RussianNamesView","RussianNamesParser",false,new String[]{"RussianNamesModule"},"RussianNamesModule.RussianNamesView","MULTILINGUAL");
  }
}
