package comjason_lewisg.httpsgithub.boozic.Handlers;

import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import comjason_lewisg.httpsgithub.boozic.MainActivity;
import comjason_lewisg.httpsgithub.boozic.Models.ProductStorageModel;
import comjason_lewisg.httpsgithub.boozic.ProductActivity;
import comjason_lewisg.httpsgithub.boozic.R;

public class DialogHandler {

    public void onCreate() {
    }

    public void OpenFeedbackDialog(final MainActivity m) {

        //Create the MaterialDialog object to start initiallizing attributes
        MaterialDialog dialog = new MaterialDialog.Builder(m)
                .title("Send us feedback")
                .inputRange(5, 250, m.getColorPrimary())
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_CLASS_TEXT)
                .input("What can we improve upon?", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        m.FC.sendFeedback(input.toString());
                    }
                })
                .positiveText("SEND")
                .negativeText("CANCEL")
                .widgetColor(m.getColorAccent())
                .positiveColor(m.getColorAccent())
                .negativeColor(m.getColorAccent())
                .build();

        EditText input = dialog.getInputEditText();
        input.setSingleLine(false);
        input.setVerticalScrollBarEnabled(true);
        input.setBackground(null);
        input.setLines(5);
        input.setGravity(Gravity.TOP);

        dialog.show();
    }

    public void OpenLegalDialog(final MainActivity m) {

        //Create the MaterialDialog object to start initiallizing attributes
        MaterialDialog dialog = new MaterialDialog.Builder(m)
                .title("Legal and Liability")
                .content("This is where the legal documentation will go.")
                .positiveText("OK")
                .positiveColor(m.getColorAccent())
                .build();

        dialog.show();
    }

    public void OpenRangeDialog(final MainActivity m, String title, final String units) {
        //Create the MaterialDialog object to start initiallizing attributes
        MaterialDialog dialog = new MaterialDialog.Builder(m)
                .title(title)
                .customView(R.layout.custom_range, true)
                .positiveText("SET")
                .positiveColor(m.getColorAccent())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        View view = dialog.getCustomView();

                        EditText low_input = (EditText) view.findViewById(R.id.range_low_input);
                        String low = low_input.getText().toString();

                        EditText high_input = (EditText) view.findViewById(R.id.range_high_input);
                        String high = high_input.getText().toString();

                        checkDialog(m, units, low, high);
                    }
                })

                .build();

        View view = dialog.getCustomView();

        EditText low_input = (EditText) view.findViewById(R.id.range_low_input);
        EditText high_input = (EditText) view.findViewById(R.id.range_high_input);

        setUnits(units, low_input, high_input, m);

        if (units.equals("$")) {
            low_input.addTextChangedListener(makeTextWatcher(low_input, units));
            high_input.addTextChangedListener(makeTextWatcher(high_input, units));
        } else if (units.equals("%")) {
            low_input.addTextChangedListener(makeTextWatcher(low_input, units));
            high_input.addTextChangedListener(makeTextWatcher(high_input, units));
        } else {
            low_input.addTextChangedListener(makeTextWatcher(low_input, units));
            high_input.addTextChangedListener(makeTextWatcher(high_input, units));
        }

        dialog.show();
    }

    public void OpenCustomMileDialog(final MainActivity m) {
        //Create the MaterialDialog object to start initiallizing attributes
        MaterialDialog dialog = new MaterialDialog.Builder(m)
                .title("Custom Mile Radius")
                .customView(R.layout.custom_mi, true)
                .positiveText("SET")
                .positiveColor(m.getColorAccent())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        View view = dialog.getCustomView();

                        EditText miles = (EditText) view.findViewById(R.id.custom_mi_input);
                        String radius = miles.getText().toString();

                        checkDialog(m, radius);
                    }
                })
                .build();

        View view = dialog.getCustomView();
        EditText miles = (EditText) view.findViewById(R.id.custom_mi_input);
        setMiles(m, miles);

        dialog.show();
    }

    public void StartProductInfoDialog(final ProductActivity p) {
        CharSequence[] items = {"Type of Product", "Volume"};
        MaterialDialog dialog = new MaterialDialog.Builder(p)
                .title("What must Change?")
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) UpdateType(p,true);
                        else if (which == 1) UpdateVolume(p);
                        return true;
                    }
                })
                .positiveText("OK")
                .widgetColor(p.getAccentColor())
                .positiveColor(p.getAccentColor())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //save selected
                    }
                })
                .build();

        dialog.show();
    }

    public void UpdateType(final ProductActivity p, final boolean cameFromStartProductInfo) {
        CharSequence[] items = {"Wine", "Beer", "Liquor", "Mixed"};
        MaterialDialog dialog = new MaterialDialog.Builder(p)
                .title("Select Product Type")
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Log.v("TYPE", "string = " + text);
                        if (text == null) UpdateType(p, cameFromStartProductInfo);
                        else p.updatedModel.updateType(which + 1);

                        if (!cameFromStartProductInfo) {
                            if (p.model.container.equals("N/A") && p.updatedModel.type == 2) UpdateContainer(p);
                            else if (p.model.abv <= 0 && p.updatedModel.type == 2) UpdateAbv(p, true);
                            else if (p.model.abv <= 0) UpdateAbv(p, false);
                        }
                        return true;
                    }
                })
                .positiveText("OK")
                .widgetColor(p.getAccentColor())
                .positiveColor(p.getAccentColor())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //save selected
                    }
                })
                .build();

        dialog.show();
    }

    public void UpdateVolume(final ProductActivity p) {
        MaterialDialog dialog = new MaterialDialog.Builder(p)
                .title("Input Volume")
                .customView(R.layout.input_abv, true)
                .positiveText("SET")
                .negativeText("CANCEL")
                .positiveColor(p.getAccentColor())
                .negativeColor(p.getAccentColor())
                .neutralColor(p.getAccentColor())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        View view = dialog.getCustomView();

                        EditText percent = (EditText) view.findViewById(R.id.abv_dia_input);
                        p.updatedModel.updateVolume(changeToDouble(percent.getText().toString().replaceAll("[oz,L,ml]", "")));
                        //save input
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .build();

        View view = dialog.getCustomView();
        EditText input = (EditText) view.findViewById(R.id.abv_dia_input);
        input.setHint("new Volume");
        switch (p.model.volumeMeasure) {
            case "ml":
                setMaxLength(input, 7);
                break;
            case "L":
                setMaxLength(input, 5);
                break;
        }
        input.addTextChangedListener(makeTextWatcher(input, p.model.volumeMeasure));

        dialog.show();
    }

    public void UpdateContainer(final ProductActivity p) {
        CharSequence[] items = {"(1) bottle", "(6) bottle", "(12) bottle", "(6) can", "(12) can", "(24) can"};
        MaterialDialog dialog = new MaterialDialog.Builder(p)
                .title("Select Container")
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Log.v("CONTAINER", "string = " + text);
                        if (text == null) UpdateContainer(p);
                        else p.updatedModel.updateContainer((String) text);
                        return true;
                    }
                })
                .positiveText("NEXT")
                .negativeText("CANCEL")
                .neutralText("SKIP")
                .widgetColor(p.getAccentColor())
                .positiveColor(p.getAccentColor())
                .negativeColor(p.getAccentColor())
                .neutralColor(p.getAccentColor())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UpdateAbv(p, true);
                        //save selected
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UpdateAbv(p, true);
                    }
                })
                .build();

        dialog.show();
    }

    public void UpdateAbv(final ProductActivity p, final boolean isBeer) {
        String tmp;
        if (isBeer) tmp = "BACK";
        else tmp = "CANCEL";

        MaterialDialog dialog = new MaterialDialog.Builder(p)
                .title("Input ABV")
                .customView(R.layout.input_abv, true)
                .positiveText("NEXT")
                .negativeText(tmp)
                .neutralText("SKIP")
                .positiveColor(p.getAccentColor())
                .negativeColor(p.getAccentColor())
                .neutralColor(p.getAccentColor())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        View view = dialog.getCustomView();

                        EditText percent = (EditText) view.findViewById(R.id.abv_dia_input);
                        p.updatedModel.updateABV(changeToDouble(percent.getText().toString().replace("%", "")));

                        UpdateStore(p, true, isBeer);
                        //save input
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UpdateStore(p, true, isBeer);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (isBeer) UpdateContainer(p);
                    }
                })
                .build();

        View view = dialog.getCustomView();
        EditText input = (EditText) view.findViewById(R.id.abv_dia_input);
        input.addTextChangedListener(makeTextWatcher(input, "%"));

        dialog.show();
    }

    public void UpdateStore(final ProductActivity p, final boolean cameFrom, final boolean isBeer) {
        String tmp;
        if (cameFrom) tmp = "BACK";
        else tmp = "CANCEL";

        final Integer[] storeIDs = p.storeIDs.toArray(new Integer[p.storeIDs.size()]);
        final CharSequence[] stores = p.stores.toArray(new CharSequence[p.stores.size()]);

        MaterialDialog dialog = new MaterialDialog.Builder(p)
                .title("Select Store")
                .items(stores)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (text == null) UpdateStore(p, cameFrom, isBeer);
                        else {
                            p.updatedModel.updateStore((String) stores[which], storeIDs[which]);
                        }
                        return true;
                    }
                })
                .positiveText("NEXT")
                .negativeText(tmp)
                .neutralText("NOT FOUND")
                .widgetColor(p.getAccentColor())
                .positiveColor(p.getAccentColor())
                .negativeColor(p.getAccentColor())
                .neutralColor(p.getAccentColor())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UpdatePrice(p, cameFrom, isBeer);
                        //save selected
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (cameFrom) UpdateAbv(p, isBeer);
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //TODO: places and distance API
                    }
                })
                .build();
        dialog.show();
    }

    public void UpdatePrice(final ProductActivity p, final boolean cameFrom, final boolean isBeer) {
        MaterialDialog dialog = new MaterialDialog.Builder(p)
                .title("Update Price")
                .customView(R.layout.input_price, true)
                .positiveText("DONE")
                .negativeText("BACK")
                .positiveColor(p.getAccentColor())
                .negativeColor(p.getAccentColor())
                .neutralColor(p.getAccentColor())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View view = dialog.getCustomView();
                        EditText price = (EditText) view.findViewById(R.id.price_dia_input);
                        p.updatedModel.updateStorePrice(changeToDouble(price.getText().toString().replace("$", "")));
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UpdateStore(p, cameFrom, isBeer);
                    }
                })
                .build();

        View view = dialog.getCustomView();
        final EditText price = (EditText) view.findViewById(R.id.price_dia_input);
        price.addTextChangedListener(makeTextWatcher(price, "$"));

        dialog.show();
    }

    private TextWatcher makeTextWatcher(final EditText text, final String units) {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            private String current = "";
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(current)){
                    text.removeTextChangedListener(this);

                    String cleanString;
                    String formatted;

                    if (units.equals("$")) {
                        cleanString = s.toString().replaceAll("[oz,L,ml, avg,$,%,.]", "");
                        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
                        formatted = NumberFormat.getCurrencyInstance().format(parsed);
                        current = formatted;
                        text.setText(formatted);
                        text.setSelection(current.length());
                    }
                    else if (units.equals("%")) {
                        cleanString = s.toString().replaceAll("[oz,L,ml, avg,$,%,.]", "");
                        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
                        NumberFormat nf = NumberFormat.getNumberInstance();
                        nf.setMinimumFractionDigits(2);
                        formatted = nf.format(parsed);
                        current = formatted;
                        text.setText(formatted + "%");
                        text.setSelection(current.length());
                    } else if (units.equals("avg")){
                        cleanString = s.toString().replaceAll("[oz,L,ml, avg,$,%,.]", "");
                        Integer parsed;
                        if (cleanString.isEmpty()) parsed = new Integer("0");
                        else parsed = new Integer(cleanString);

                        if (parsed % 10 >= 5) parsed = 5;
                        else parsed = parsed % 10;

                        NumberFormat nf = NumberFormat.getNumberInstance();
                        nf.setMinimumIntegerDigits(1);
                        formatted = nf.format(parsed);
                        current = formatted;
                        text.setText(formatted + " avg");
                        text.setSelection(current.length());
                    } else if (units.equals("ml")) {
                        cleanString = s.toString().replaceAll("[oz,L,ml, avg,$,%,.]", "");
                        if (cleanString.isEmpty()) cleanString = "0.0";
                        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(10), BigDecimal.ROUND_FLOOR);
                        NumberFormat nf = NumberFormat.getNumberInstance();
                        nf.setMinimumFractionDigits(1);
                        formatted = nf.format(parsed);
                        current = formatted;
                        text.setText(formatted + "ml");
                        text.setSelection(current.length());
                    } else if (units.equals("L")) {
                        if (text.length() <= 3 || text.getText().toString().charAt(0) == '0') {
                            cleanString = s.toString().replaceAll("[oz,L,ml, avg,$,%,.]", "");
                            if (cleanString.isEmpty()) cleanString = "0.0";
                            BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(10), BigDecimal.ROUND_FLOOR);
                            NumberFormat nf = NumberFormat.getNumberInstance();
                            nf.setMinimumFractionDigits(1);
                            formatted = nf.format(parsed);
                            current = formatted;
                            text.setText(formatted + "L");
                            text.setSelection(current.length());
                        }
                    } else if (units.equals("oz")) {
                        cleanString = s.toString().replaceAll("[oz,L,ml, avg,$,%,.]", "");
                        if (cleanString.isEmpty()) cleanString = "0.0";
                        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(10), BigDecimal.ROUND_FLOOR);
                        NumberFormat nf = NumberFormat.getNumberInstance();
                        nf.setMinimumFractionDigits(1);
                        formatted = nf.format(parsed);
                        current = formatted;
                        text.setText(formatted + "oz");
                        text.setSelection(current.length());
                    }

                    text.addTextChangedListener(this);
                }
            }
        };

        return tw;
    }
    private void setMaxLength(EditText input, int maxLength) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        input.setFilters(FilterArray);
    }

    private void setMiles(MainActivity m, EditText miles) {
        miles.setText(m.FMHandle.custommi_miles + "");
    }

    private void setUnits(String units, EditText low_input, EditText high_input, MainActivity m) {

        DecimalFormat formatter = new DecimalFormat("#0.00");

        switch (units) {
            case "$":
                low_input.setText("$" + formatter.format(m.FMHandle.pricerange_low));
                high_input.setText("$" + formatter.format(m.FMHandle.pricerange_high));
                break;
            case "%":
                low_input.setText(formatter.format(m.FMHandle.contentrange_low) + "%");
                high_input.setText(formatter.format(m.FMHandle.contentrange_high) + "%");
                break;
            case "avg":
                low_input.setText(m.FMHandle.ratingrange_low + " avg");
                high_input.setText(m.FMHandle.ratingrange_high + " avg");
                break;
        }
    }

    private void checkDialog(MainActivity m, String radius) {
        //set the filterbutton model's low/high variables
        m.FMHandle.setCustommi(changeToInt(radius));
    }

    private void checkDialog(MainActivity m, String units, String low, String high) {

        int lowInt;
        int highInt;
        int tmpInt;
        double lowDouble;
        double highDouble;
        double tmpDouble;

        //set the filterbutton model's low/high variables
        switch (units) {
            case "$":
                lowDouble = changeToDouble(low.replace("$", ""));
                highDouble = changeToDouble(high.replace("$", ""));
                //if user fucks up the input...
                if (highDouble < lowDouble) {
                    tmpDouble = lowDouble;
                    lowDouble = highDouble;
                    highDouble = tmpDouble;
                }
                m.FMHandle.setPriceRange((float)lowDouble, (float)highDouble);
                break;
            case "%":
                lowDouble = changeToDouble(low.replace("%", ""));
                highDouble = changeToDouble(high.replace("%", ""));
                //if user fucks up the input...
                if (highDouble < lowDouble) {
                    tmpDouble = lowDouble;
                    lowDouble = highDouble;
                    highDouble = tmpDouble;
                }
                m.FMHandle.setContentRange((float)lowDouble, (float)highDouble);
                break;
            case "avg":
                lowInt = changeToInt(low.replace(" avg",""));
                highInt = changeToInt(high.replace(" avg",""));
                //if user fucks up the input...
                if (highInt < lowInt) {
                    tmpInt = lowInt;
                    lowInt = highInt;
                    highInt = tmpInt;
                }
                m.FMHandle.setRatingRange(lowInt, highInt);
                break;
        }
    }

    private CharSequence[] appendCharSequences(CharSequence[] stores, CharSequence[] addresses) {
        CharSequence[] combined = new CharSequence[stores.length];

        for (int i = 0; i < stores.length; i++) {
            combined[i] = stores[i] + " at " + addresses[i];
        }
        return combined;
    }

    private int changeToInt(String str) {
        return Integer.parseInt(str);
    }
    public double changeToDouble(String str) {
        return Double.parseDouble(str);
    }
}