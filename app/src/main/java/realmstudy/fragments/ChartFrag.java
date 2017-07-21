package realmstudy.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import realmstudy.R;
import realmstudy.adapter.OverRvAdapter;
import realmstudy.data.OverAdapterData;

/**
 * Created by developer on 12/7/17.
 */

public class ChartFrag extends Fragment {

    private BarChart b_chart;
    private ArrayList<BarEntry> barValueSet1;
    private ArrayList<BarEntry> barValueSet2;
    private List<OverAdapterData> overAdapterData;
    float groupSpace = 0.0f;
    float barSpace = 0.00f;
    private LineChart l_chart;
    private LineDataSet line1, line2;
    private ArrayList<Entry> LineValue1;
    private ArrayList<Entry> LineValue2;
    private String firstBattedTeam, secBattedTeam;
    private List<OverAdapterData> firstInningsOverAdapterData;
    private List<OverAdapterData> secInningsOverAdapterData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chart_layout, container, false);
        b_chart = (BarChart) v.findViewById(R.id.chart);
        l_chart = (LineChart) v.findViewById(R.id.linechart);

        return v;
    }

    public void setData(List<OverAdapterData> datas, String firstBatTeam, String secBatTeam) {
        overAdapterData = datas;
        for (int i = 0; i < overAdapterData.size(); i++) {
            if (overAdapterData.get(i).getOver() == 1)
                if (i != overAdapterData.size() - 1) {
                    secInningsOverAdapterData = overAdapterData.subList(0, i);
                    firstInningsOverAdapterData = overAdapterData.subList(i + 1, overAdapterData.size() - 1);
                } else {
                    if (secInningsOverAdapterData == null)
                        firstInningsOverAdapterData = overAdapterData.subList(0, overAdapterData.size() - 1);
                }
        }
        System.out.println("________sss" + datas.size());
        OverRvAdapter adapter = new OverRvAdapter(getActivity(), datas);
        firstBattedTeam = firstBatTeam;
        secBattedTeam = secBatTeam;
        setBarData();
        setBarStyle();

        setLineData();
        setLineStyle();
    }

    private void setLineStyle() {
        YAxis line_leftAxis = l_chart.getAxisLeft();
        line_leftAxis.setAxisMaxValue(50);
        line_leftAxis.setAxisMinValue(0);
        XAxis xAxis = l_chart.getXAxis();
        xAxis.setAxisMaxValue(overAdapterData.size());
        xAxis.setAxisMinValue(0);
//

        line1 = new LineDataSet(LineValue1, firstBattedTeam);
        line1.setColor(Color.RED);

        line2 = new LineDataSet(LineValue2, secBattedTeam);
        line2.setColor(Color.GREEN);

        ArrayList<ILineDataSet> dataSets1 = new ArrayList<ILineDataSet>();
        dataSets1.add(line1);
        dataSets1.add(line2);
        LineData line_data = new LineData(dataSets1);
        l_chart.setTouchEnabled(true);
        line_data.setDrawValues(false);
        l_chart.setData(line_data);
        l_chart.invalidate();
    }

    private void setLineData() {

        YAxis line_right = l_chart.getAxisRight();
        line_right.setEnabled(false);


        XAxis line_xAxis = l_chart.getXAxis();
        line_xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        LineValue1 = new ArrayList<Entry>();
        if (firstInningsOverAdapterData != null)
            for (int i = firstInningsOverAdapterData.size() - 1; i >= 0; i--) {
                System.out.println("toototo" + i + "__" + firstInningsOverAdapterData.get(i).getTotal_run());
                LineValue1.add(new Entry(i, firstInningsOverAdapterData.get(i).getTotal_run()));
            }
        LineValue2 = new ArrayList<Entry>();
        if (secInningsOverAdapterData != null)
            for (int i = 0; i < overAdapterData.size(); i++) {
                System.out.println("toototo" + i + "__" + secInningsOverAdapterData.get(i).getTotal_run());
                LineValue2.add(new Entry(i, secInningsOverAdapterData.get(i).getTotal_run() + 3));
            }


    }

    private void setBarStyle() {

        XAxis xAxis = b_chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis right = b_chart.getAxisRight();
        right.setEnabled(false);
        BarDataSet set1 = new BarDataSet(barValueSet1, firstBattedTeam);
        set1.setColor(Color.RED);

        set1.setBarBorderWidth(0.5f);
        BarDataSet set2 = new BarDataSet(barValueSet2, secBattedTeam);
        set2.setColor(Color.GREEN);
        set2.setBarBorderWidth(0.5f);
        set2.setBarBorderColor(Color.WHITE);
        set1.setBarBorderColor(Color.WHITE);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        BarData data = new BarData(dataSets);

        b_chart.setTouchEnabled(true);
        b_chart.setFitBars(true);
        b_chart.setData(data);

        b_chart.setPinchZoom(false);
        b_chart.setBackgroundColor(Color.WHITE); //set whatever color you prefer
        b_chart.setDrawGridBackground(false);
        b_chart.getAxisLeft().setDrawGridLines(false);
        b_chart.getXAxis().setDrawGridLines(false);
        b_chart.getAxisLeft().setDrawAxisLine(false);
        b_chart.getXAxis().setDrawAxisLine(false);
        b_chart.setDrawValueAboveBar(true);
        //   b_chart.setPinchZoom(true);
        b_chart.groupBars(0, groupSpace, barSpace);
        b_chart.invalidate();
    }

    private void setBarData() {


        barValueSet1 = new ArrayList<>();
        barValueSet2 = new ArrayList<>();
        if (firstInningsOverAdapterData != null)
            for (int i = firstInningsOverAdapterData.size() - 1; i >= 0; i--) {
                System.out.println("toototo" + i + "__" + firstInningsOverAdapterData.get(i).getTotal_run());
                BarEntry v1e1 = new BarEntry(i, firstInningsOverAdapterData.get(i).getTotal_run());
                barValueSet1.add(v1e1);
            }

//        BarEntry v1e1x = new BarEntry(overAdapterData.length, 0);
//        BarEntry v1e1xs = new BarEntry(overAdapterData.length+1, 0);
//        barValueSet1.add(v1e1x);
//        barValueSet1.add(v1e1xs);
        if (secInningsOverAdapterData != null)
            for (int i = 0; i < secInningsOverAdapterData.size(); i++) {

                BarEntry v1e1 = new BarEntry(i, secInningsOverAdapterData.get(i).getTotal_run() - 3);
                barValueSet2.add(v1e1);
            }
//        barValueSet2.add(v1e1x);
//        barValueSet2.add(v1e1xs);
    }


}
