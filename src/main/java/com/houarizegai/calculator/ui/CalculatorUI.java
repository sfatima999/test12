package com.houarizegai.calculator.ui;

import com.houarizegai.calculator.theme.properties.Theme;
import com.houarizegai.calculator.theme.ThemeLoader;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;
import java.util.regex.Pattern;
import java.awt.Color;
import javax.swing.*;

import static com.houarizegai.calculator.util.ColorUtil.hex2Color;

public class CalculatorUI {

    private static final String FONT_NAME = "Comic Sans MS";
    private static final String DOUBLE_OR_NUMBER_REGEX = "([-]?\\d+[.]\\d*)|(\\d+)|(-\\d+)";
    private static final String APPLICATION_TITLE = "Calculator";
    private static final int WINDOW_WIDTH = 410;
    private static final int WINDOW_HEIGHT = 600;
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 70;
    private static final int MARGIN_X = 20;
    private static final int MARGIN_Y = 60;

    private final JFrame window;
    private JComboBox<String> comboCalculatorType;
    private JComboBox<String> comboTheme;
    private JTextField inputScreen;
    private JButton btnC;
    private JButton btnBack;
    private JButton btnMod;
    private JButton btnDiv;
    private JButton btnMul;
    private JButton btnSub;
    private JButton btnAdd;
    private JButton btn0;
    private JButton btn1;
    private JButton btn2;
    private JButton btn3;
    private JButton btn4;
    private JButton btn5;
    private JButton btn6;
    private JButton btn7;
    private JButton btn8;
    private JButton btn9;
    private JButton btnPoint;
    private JButton btnEqual;
    private JButton btnRoot;
    private JButton btnPower;
    private JButton btnLog;

    private char selectedOperator = ' ';
    private boolean go = true; // For calculate with Opt != (=)
    private boolean addToDisplay = true; // Connect numbers in display
    private double typedValue = 0;

    private final Map<String, Theme> themesMap;

    public CalculatorUI() {
        themesMap = ThemeLoader.loadThemes();

        window = new JFrame(APPLICATION_TITLE);
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);

        int[] columns = {MARGIN_X, MARGIN_X + 90, MARGIN_X + 90 * 2, MARGIN_X + 90 * 3, MARGIN_X + 90 * 4};
        int[] rows = {MARGIN_Y, MARGIN_Y + 100, MARGIN_Y + 100 + 80, MARGIN_Y + 100 + 80 * 2, MARGIN_Y + 100 + 80 * 3, MARGIN_Y + 100 + 80 * 4};

        initInputScreen(columns, rows);
        initButtons(columns, rows);
        initCalculatorTypeSelector();

        initThemeSelector();

        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public double calculate(double firstNumber, double secondNumber, char operator) {
        switch (operator) {
            case '+':
                return firstNumber + secondNumber;
            case '-':
                return firstNumber - secondNumber;
            case '*':
                return firstNumber * secondNumber;
            case '/':
                return firstNumber / secondNumber;
            case '%':
                return firstNumber % secondNumber;
            case '^':
                return Math.pow(firstNumber, secondNumber);
            default:
                return secondNumber;
        }
    }

    private void initThemeSelector() {
        comboTheme = createComboBox(themesMap.keySet().toArray(new String[0]), 230, 30, "Theme");
        comboTheme.addItemListener(event -> {
            if (event.getStateChange() != ItemEvent.SELECTED)
                return;

            String selectedTheme = (String) event.getItem();
            applyTheme(themesMap.get(selectedTheme));
        });

        if (themesMap.entrySet().iterator().hasNext()) {
            applyTheme(themesMap.entrySet().iterator().next().getValue());
        }
    }

    private void initInputScreen(int[] columns, int[] rows) {
        inputScreen = new JTextField("0");
        inputScreen.setBounds(columns[0], rows[0], 350, 70);
        inputScreen.setEditable(false);
        inputScreen.setBackground(Color.WHITE);
        inputScreen.setFont(new Font(FONT_NAME, Font.PLAIN, 33));
        window.add(inputScreen);
    }

    private void initCalculatorTypeSelector() {
        comboCalculatorType = createComboBox(new String[]{"Standard", "Scientific"}, 20, 30, "Calculator type");
        comboCalculatorType.addItemListener(event -> {
            if (event.getStateChange() != ItemEvent.SELECTED)
                return;

            String selectedItem = (String) event.getItem();
            switch (selectedItem) {
                case "Standard":
                    window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
                    btnRoot.setVisible(false);
                    btnPower.setVisible(false);
                    btnLog.setVisible(false);
                    break;
                case "Scientific":
                    window.setSize(WINDOW_WIDTH + 80, WINDOW_HEIGHT);
                    btnRoot.setVisible(true);
                    btnPower.setVisible(true);
                    btnLog.setVisible(true);
                    break;
            }
        });
    }

    // cleaned up this method by breaking everything into sub-methods
    private void initButtons(int[] columns, int[] rows) {
        initClearButton(columns, rows);
        initBackButton(columns, rows);
        initModButton(columns, rows);
        initDigitButtons(columns, rows);
        initArithmeticOperatorButtons(columns, rows);
        initDecimalButton(columns, rows);
        initEqualButton(columns, rows);
        initScientificButtons(columns, rows);
    }

    private void initClearButton(int[] columns, int[] rows) {
        btnC = createButton("C", columns[0], rows[1]);
        btnC.addActionListener(event -> onClearPressed());
    }

    private void initBackButton(int[] columns, int[] rows) {
        btnBack = createButton("<-", columns[1], rows[1]);
        btnBack.addActionListener(event -> onBackPressed());
    }

    private void initModButton(int[] columns, int[] rows) {
        btnMod = createButton("%", columns[2], rows[1]);
        btnMod.addActionListener(event -> onModPressed());
    }

    private void initDigitButtons(int[] columns, int[] rows) {
        btn7 = addDigitButton("7", columns[0], rows[2]);
        btn8 = addDigitButton("8", columns[1], rows[2]);
        btn9 = addDigitButton("9", columns[2], rows[2]);
        btn4 = addDigitButton("4", columns[0], rows[3]);
        btn5 = addDigitButton("5", columns[1], rows[3]);
        btn6 = addDigitButton("6", columns[2], rows[3]);
        btn1 = addDigitButton("1", columns[0], rows[4]);
        btn2 = addDigitButton("2", columns[1], rows[4]);
        btn3 = addDigitButton("3", columns[2], rows[4]);
        btn0 = addDigitButton("0", columns[1], rows[5]);
    }

    private JButton addDigitButton(String digit, int x, int y) {
        JButton btn = createButton(digit, x, y);
        btn.addActionListener(event -> onDigitPressed(digit));
        return btn;
    }

    private void initArithmeticOperatorButtons(int[] columns, int[] rows) {
        btnDiv = addOperatorButton("/", '/', columns[3], rows[1]);
        btnMul = addOperatorButton("*", '*', columns[3], rows[2]);
        btnSub = addOperatorButton("-", '-', columns[3], rows[3]);
        btnAdd = addOperatorButton("+", '+', columns[3], rows[4]);
    }

    private JButton addOperatorButton(String label, char operator, int x, int y) {
        JButton btn = createButton(label, x, y);
        btn.addActionListener(event -> onBinaryOperatorPressed(operator));
        return btn;
    }

    private void initDecimalButton(int[] columns, int[] rows) {
        btnPoint = createButton(".", columns[0], rows[5]);
        btnPoint.addActionListener(event -> onDecimalPressed());
    }

    private void initEqualButton(int[] columns, int[] rows) {
        btnEqual = createButton("=", columns[2], rows[5]);
        btnEqual.addActionListener(event -> onEqualPressed());
        btnEqual.setSize(2 * BUTTON_WIDTH + 10, BUTTON_HEIGHT);
    }

    private void initScientificButtons(int[] columns, int[] rows) {
        btnRoot = createButton("√", columns[4], rows[1]);
        btnRoot.addActionListener(event -> onUnaryOperatorPressed('√', Math::sqrt));
        btnRoot.setVisible(false);

        btnPower = addOperatorButton("pow", '^', columns[4], rows[2]);
        btnPower.setFont(new Font(FONT_NAME, Font.PLAIN, 24));
        btnPower.setVisible(false);

        btnLog = createButton("ln", columns[4], rows[3]);
        btnLog.addActionListener(event -> onUnaryOperatorPressed('l', Math::log));
        btnLog.setVisible(false);
    }

    private void onClearPressed() {
        inputScreen.setText("0");
        selectedOperator = ' ';
        typedValue = 0;
    }

    private void onBackPressed() {
        String text = inputScreen.getText();
        if (text.length() <= 1) {
            inputScreen.setText("0");
            return;
        }
        inputScreen.setText(text.substring(0, text.length() - 1));
    }

    private void onDigitPressed(String digit) {
        if (addToDisplay) {
            if (Pattern.matches("[0]*", inputScreen.getText())) {
                inputScreen.setText(digit);
            } else {
                inputScreen.setText(inputScreen.getText() + digit);
            }
        } else {
            inputScreen.setText(digit);
            addToDisplay = true;
        }
        go = true;
    }

    private void onBinaryOperatorPressed(char operator) {
        if (!Pattern.matches(DOUBLE_OR_NUMBER_REGEX, inputScreen.getText()))
            return;

        if (go) {
            typedValue = calculate(typedValue, Double.parseDouble(inputScreen.getText()), selectedOperator);
            updateDisplay(typedValue);
            selectedOperator = operator;
            go = false;
            addToDisplay = false;
        } else {
            selectedOperator = operator;
        }
    }

    private void onUnaryOperatorPressed(char opChar, DoubleUnaryOperator function) {
        if (!Pattern.matches(DOUBLE_OR_NUMBER_REGEX, inputScreen.getText()))
            return;

        if (go) {
            typedValue = function.applyAsDouble(Double.parseDouble(inputScreen.getText()));
            updateDisplay(typedValue);
            selectedOperator = opChar;
            addToDisplay = false;
        }
    }

    private void onModPressed() {
        if (!Pattern.matches(DOUBLE_OR_NUMBER_REGEX, inputScreen.getText()) || !go)
            return;

        typedValue = calculate(typedValue, Double.parseDouble(inputScreen.getText()), selectedOperator);
        updateDisplay(typedValue);
        selectedOperator = '%';
        go = false;
        addToDisplay = false;
    }

    private void onEqualPressed() {
        if (!Pattern.matches(DOUBLE_OR_NUMBER_REGEX, inputScreen.getText()))
            return;

        if (go) {
            typedValue = calculate(typedValue, Double.parseDouble(inputScreen.getText()), selectedOperator);
            updateDisplay(typedValue);
            selectedOperator = '=';
            addToDisplay = false;
        }
    }

    private void onDecimalPressed() {
        if (addToDisplay) {
            if (!inputScreen.getText().contains(".")) {
                inputScreen.setText(inputScreen.getText() + ".");
            }
        } else {
            inputScreen.setText("0.");
            addToDisplay = true;
        }
        go = true;
    }

    private void updateDisplay(double value) {
        if (Pattern.matches("-?\\d+[.]0*", String.valueOf(value))) {
            inputScreen.setText(String.valueOf((int) value));
        } else {
            inputScreen.setText(String.valueOf(value));
        }
    }

    private JComboBox<String> createComboBox(String[] items, int x, int y, String toolTip) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBounds(x, y, 140, 25);
        combo.setToolTipText(toolTip);
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        window.add(combo);

        return combo;
    }

    private JButton createButton(String label, int x, int y) {
        JButton btn = new JButton(label);
        btn.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        btn.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusable(false);
        window.add(btn);

        return btn;
    }

    private void applyTheme(Theme theme) {
        window.getContentPane().setBackground(hex2Color(theme.getApplicationBackground()));

        comboCalculatorType.setForeground(hex2Color(theme.getTextColor()));
        comboTheme.setForeground(hex2Color(theme.getTextColor()));
        inputScreen.setForeground(hex2Color(theme.getTextColor()));
        btn0.setForeground(hex2Color(theme.getTextColor()));
        btn1.setForeground(hex2Color(theme.getTextColor()));
        btn2.setForeground(hex2Color(theme.getTextColor()));
        btn3.setForeground(hex2Color(theme.getTextColor()));
        btn4.setForeground(hex2Color(theme.getTextColor()));
        btn5.setForeground(hex2Color(theme.getTextColor()));
        btn6.setForeground(hex2Color(theme.getTextColor()));
        btn7.setForeground(hex2Color(theme.getTextColor()));
        btn8.setForeground(hex2Color(theme.getTextColor()));
        btn9.setForeground(hex2Color(theme.getTextColor()));
        btnPoint.setForeground(hex2Color(theme.getTextColor()));
        btnC.setForeground(hex2Color(theme.getTextColor()));
        btnBack.setForeground(hex2Color(theme.getTextColor()));
        btnMod.setForeground(hex2Color(theme.getTextColor()));
        btnDiv.setForeground(hex2Color(theme.getTextColor()));
        btnMul.setForeground(hex2Color(theme.getTextColor()));
        btnSub.setForeground(hex2Color(theme.getTextColor()));
        btnAdd.setForeground(hex2Color(theme.getTextColor()));
        btnRoot.setForeground(hex2Color(theme.getTextColor()));
        btnLog.setForeground(hex2Color(theme.getTextColor()));
        btnPower.setForeground(hex2Color(theme.getTextColor()));
        btnEqual.setForeground(hex2Color(theme.getBtnEqualTextColor()));

        comboCalculatorType.setBackground(hex2Color(theme.getApplicationBackground()));
        comboTheme.setBackground(hex2Color(theme.getApplicationBackground()));
        inputScreen.setBackground(hex2Color(theme.getApplicationBackground()));
        btn0.setBackground(hex2Color(theme.getNumbersBackground()));
        btn1.setBackground(hex2Color(theme.getNumbersBackground()));
        btn2.setBackground(hex2Color(theme.getNumbersBackground()));
        btn3.setBackground(hex2Color(theme.getNumbersBackground()));
        btn4.setBackground(hex2Color(theme.getNumbersBackground()));
        btn5.setBackground(hex2Color(theme.getNumbersBackground()));
        btn6.setBackground(hex2Color(theme.getNumbersBackground()));
        btn7.setBackground(hex2Color(theme.getNumbersBackground()));
        btn8.setBackground(hex2Color(theme.getNumbersBackground()));
        btn9.setBackground(hex2Color(theme.getNumbersBackground()));
        btnPoint.setBackground(hex2Color(theme.getNumbersBackground()));
        btnC.setBackground(hex2Color(theme.getOperatorBackground()));
        btnBack.setBackground(hex2Color(theme.getOperatorBackground()));
        btnMod.setBackground(hex2Color(theme.getOperatorBackground()));
        btnDiv.setBackground(hex2Color(theme.getOperatorBackground()));
        btnMul.setBackground(hex2Color(theme.getOperatorBackground()));
        btnSub.setBackground(hex2Color(theme.getOperatorBackground()));
        btnAdd.setBackground(hex2Color(theme.getOperatorBackground()));
        btnRoot.setBackground(hex2Color(theme.getOperatorBackground()));
        btnLog.setBackground(hex2Color(theme.getOperatorBackground()));
        btnPower.setBackground(hex2Color(theme.getOperatorBackground()));
        btnEqual.setBackground(hex2Color(theme.getBtnEqualBackground()));
    }
}