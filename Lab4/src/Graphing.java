package lab4;

import lab3.Function;
import lab3.Lagrange;
import lombok.AllArgsConstructor;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;
import java.awt.*;

@AllArgsConstructor
public class Graphing {

    private double[] xData;
    private double[] yData;

    public JPanel getChart(int width, int height){
        XYChart chart = new XYChart(width, height);

        XYSeries nodes = chart.addSeries("Решение ", xData, yData);
        nodes.setShowInLegend(false);
        nodes.setMarkerColor(Color.RED);
        nodes.setLineColor(Color.WHITE);
        nodes.setMarker(SeriesMarkers.CIRCLE);


        if (xData.length <= 11) {
            Lagrange lagrangePolynomial = new Lagrange();
            Function interpolateFunction = lagrangePolynomial.interpolate(xData, yData);
            double[] xGraphing = new double[width];
            double[] yGraphing = new double[width];
            double step = Math.abs(xData[xData.length - 1] - xData[0]) / width;
            double startX = Math.min(xData[0], xData[xData.length - 1]);
            for(int i = 0; i < xGraphing.length; i++){
                xGraphing[i] = startX + step * i;
                yGraphing[i] = interpolateFunction.getValue(xGraphing[i]);
            }
            XYSeries solution = chart.addSeries("Решение", xGraphing, yGraphing);
            solution.setMarker(SeriesMarkers.NONE);
            solution.setLineColor(Color.BLUE);
        }
        else{
            nodes.setLineColor(Color.BLUE);
            nodes.setMarker(SeriesMarkers.NONE);
            nodes.setShowInLegend(true);
        }

        return new XChartPanel<>(chart);

    }
}
