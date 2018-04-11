package sample;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MathProblem {

    private final StringProperty userMathProblemInput;

    //default constructor
    public MathProblem() {
        this(null);
    }

    public MathProblem(String userMathProblemInput) {
        this.userMathProblemInput = new SimpleStringProperty(userMathProblemInput);
    }

    public String getUserMathProblemInput( ) {
        return userMathProblemInput.get();
    }

    public void setUserMathProblemInput(String userMathProblemInput) {
        this.userMathProblemInput.set(userMathProblemInput);
    }

}//END OF CLASS
