import functions.Function;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;

public class Main extends Application {


    NumberTextField A;
    NumberTextField B;
    NumberTextField C;
    NumberTextField D;
    RadioButton alpha;
    RadioButton beta;
    RadioButton gamma;
    RadioButton delta;
    NumberTextField alphaField;
    NumberTextField betaField;
    NumberTextField gammaField;
    NumberTextField deltaField;
    NumberTextField p;
    NumberTextField n;
    NumberTextField m;
    LineChart<Number, Number> chart;
    HBox root;

    NumberTextField x0;

    NumberTextField step;
    Label integral;


    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setScene(initScene());
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Scene initScene() {
        root = new HBox();

        VBox leftPane = new VBox();
        leftPane.setMinWidth(300);
        leftPane.setPadding(new Insets(10, 10, 10, 10));
        leftPane.setSpacing(10);
        Label wParams = new Label("Window parameters");

        GridPane wGrid = new GridPane();
        wGrid.setHgap(10);
        wGrid.setVgap(10);
        Label left = new Label("Left:");
        A = new NumberTextField(-2);
        Label top = new Label("Top:");
        B = new NumberTextField(2);
        Label right = new Label("Right:");
        C = new NumberTextField(2);
        Label bottom = new Label("Bottom:");
        D = new NumberTextField(-2);
        wGrid.add(left, 0, 0);
        wGrid.add(A, 1, 0);
        wGrid.add(right, 0, 1);
        wGrid.add(C, 1, 1);
        wGrid.add(bottom, 0, 2);
        wGrid.add(D, 1, 2);
        wGrid.add(top, 0, 3);
        wGrid.add(B, 1, 3);

        Label fParams = new Label("Function parameters");

        GridPane fGrid = new GridPane();
        fGrid.setHgap(10);
        fGrid.setVgap(10);
        Label alphaL = new Label("Alpha:");
        alpha = new RadioButton();
        alphaField = new NumberTextField(1);
        Label betaL = new Label("Beta:");
        beta = new RadioButton();
        betaField = new NumberTextField(1);
        Label gammaL = new Label("Gamma:");
        gamma = new RadioButton();
        gammaField = new NumberTextField(1);
        Label deltaL = new Label("Delta:");
        delta = new RadioButton();
        deltaField = new NumberTextField(1);
        x0 = new NumberTextField(0);
        Label x0L = new Label("x0:");
        fGrid.add(alphaL, 0, 0);
        fGrid.add(alpha, 1, 0);
        fGrid.add(alphaField, 2, 0);
        fGrid.add(betaL, 0, 1);
        fGrid.add(beta, 1, 1);
        fGrid.add(betaField, 2, 1);
        fGrid.add(gammaL, 0, 2);
        fGrid.add(gamma, 1, 2);
        fGrid.add(gammaField, 2, 2);
        fGrid.add(deltaL, 0, 3);
        fGrid.add(delta, 1, 3);
        fGrid.add(deltaField, 2, 3);
        fGrid.add(x0L, 0, 4);
        fGrid.add(x0, 1, 4);

        ToggleGroup group = new ToggleGroup();
        alpha.setToggleGroup(group);
        beta.setToggleGroup(group);
        gamma.setToggleGroup(group);
        delta.setToggleGroup(group);
        alpha.setSelected(true);

        GridPane bGrid = new GridPane();
        bGrid.setHgap(10);
        bGrid.setVgap(10);
        Label pBoundL = new Label("p:");
        p = new NumberTextField(1);
        Label nBoundL = new Label("n:");
        n = new NumberTextField(500);
        Label mBoundL = new Label("m:");
        m = new NumberTextField(200);
        bGrid.add(pBoundL, 0, 0);
        bGrid.add(p, 1, 0);
        bGrid.add(nBoundL, 0, 1);
        bGrid.add(n, 1, 1);
        bGrid.add(mBoundL, 0, 2);
        bGrid.add(m, 1, 2);

        GridPane res = new GridPane();
        step = new NumberTextField(0.1);
        integral = new Label();
        res.add(new Label("Step = "),0,0);
        res.add(step,1,0);


        Scene scene = new Scene(root, 1300, 700);

        Button commit = new Button("Draw");
        commit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initPlot();
            }
        });

        Label name = new Label("ИВТ-42БО Суендуков Михаил.");
        name.setAlignment(Pos.BOTTOM_LEFT);

        leftPane.getChildren().addAll(wParams, wGrid, fParams, fGrid, bGrid, res, commit, name);

        root.getChildren().add(leftPane);

        initPlot();

        return scene;
    }

    public void initPlot() {
        Function f = new Function() {
            @Override
            public double getValueAt(double x, double y) {
                double a = alphaField.getNumber();
                double b = betaField.getNumber();
                double g = gammaField.getNumber();
                double d = deltaField.getNumber();
                if(alpha.isSelected()){
                    a = x;
                }
                if(beta.isSelected()){
                    b = x;
                }
                if(gamma.isSelected()){
                    g = x;
                }
                if(delta.isSelected()){
                    d = x;
                }

                return a * (Math.sin(b / ((y - g) * (y - g)))) * Math.cos(d * 1);
            }
        };

        if(root.getChildren().contains(chart)) {
            root.getChildren().remove(chart);
        }

        chart = new LineChart<Number, Number>(new NumberAxis(A.getNumber(), C.getNumber(),(C.getNumber()-A.getNumber())/10), new NumberAxis(D.getNumber(), B.getNumber(),(B.getNumber()-D.getNumber())/10));
        chart.setMinWidth(1000);
        chart.setCreateSymbols(true);
        chart.getStylesheets().add(Main.class.getResource("res/ch.css").toExternalForm());
        root.getChildren().add(chart);

        double step = this.step.getNumber();
        double x0 = this.x0.getNumber();
        double xn = x0;
        int m = (int) this.m.getNumber();
        int n = (int) this.n.getNumber();
        int p = (int) this.p.getNumber();
        ArrayList<ArrayList<Pair<Double, Double>>> data = new ArrayList<>();
        ArrayList<Pair<Double, Double>> mainLine = new ArrayList<>();
        ArrayList<Pair<Double, Double>> pLine = new ArrayList<>();
        data.add(mainLine);
        data.add(pLine);
        for (double j = A.getNumber(); j < C.getNumber(); j+=step) {
            xn = x0;
            for(int i = 0;i<m+n;i++){
                if(i>m){
                    mainLine.add(new Pair<>(j, xn));
                    if(i%p==0) {
                        pLine.add(new Pair<>(j, xn));
                    }
                }
                xn = f.getValueAt(j,xn);
            }
        }

        for(ArrayList<Pair<Double, Double>> fData : data){
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (int i = 0; i < fData.size(); i++) {
                Number x = fData.get(i).getKey();
                Number y = fData.get(i).getValue();
                series.getData().add(new XYChart.Data<>(x, y));
            }
            chart.getData().add(series);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
