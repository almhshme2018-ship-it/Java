package com.yemeni.huroofna;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private LinearLayout root;
    private SharedPreferences prefs;
    private TextToSpeech tts;

    private String studentName = "";
    private int stars = 0;

    private final String[] letters = {
            "ا","ب","ت","ث","ج","ح","خ","د","ذ","ر","ز","س","ش",
            "ص","ض","ط","ظ","ع","غ","ف","ق","ك","ل","م","ن","هـ","و","ي"
    };

    private final String[] letterNames = {
            "ألف","باء","تاء","ثاء","جيم","حاء","خاء","دال","ذال",
            "راء","زاي","سين","شين","صاد","ضاد","طاء","ظاء","عين",
            "غين","فاء","قاف","كاف","لام","ميم","نون","هاء","واو","ياء"
    };

    private final String[] words = {
            "أسد","بطة","تفاحة","ثعلب","جمل","حصان","خروف","دب",
            "ذرة","رمان","زهرة","سمكة","شمس","صقر","ضفدع","طائرة",
            "ظرف","عنب","غزال","فيل","قمر","كتاب","ليمون","موز",
            "نحلة","هلال","وردة","يد"
    };

    private final String[] emoji = {
            "🦁","🦆","🍎","🦊","🐪","🐎","🐑","🐻",
            "🌽","🍎","🌸","🐟","☀️","🦅","🐸","✈️",
            "✉️","🍇","🦌","🐘","🌙","📖","🍋","🍌",
            "🐝","🌙","🌹","✋"
    };

    private final String[] numbers = {
            "١","٢","٣","٤","٥","٦","٧","٨","٩","١٠"
    };

    private final String[] numberPictures = {
            "🍎","🍎🍎","🍎🍎🍎","🍎🍎🍎🍎",
            "🍎🍎🍎🍎🍎","⭐️⭐️⭐️⭐️⭐️⭐️",
            "🌸🌸🌸🌸🌸🌸🌸",
            "🟢🟢🟢🟢🟢🟢🟢🟢",
            "🐟🐟🐟🐟🐟🐟🐟🐟🐟",
            "⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        prefs = getSharedPreferences("HuroofnaData", MODE_PRIVATE);

        studentName = prefs.getString("student", "");
        stars = prefs.getInt("stars", 0);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("ar"));
                tts.setSpeechRate(0.75f);
            }
        });

        if (studentName.isEmpty()) {
            askStudentName();
        } else {
            showHome();
        }
    }

    private void askStudentName() {
        final EditText input = new EditText(this);
        input.setHint("اكتب اسم الطالب");
        input.setGravity(Gravity.CENTER);
        input.setTextSize(20);

        new AlertDialog.Builder(this)
                .setTitle("مرحبًا بك في حروفنا 🌟")
                .setMessage("اكتب اسم الطالب للبدء")
                .setView(input)
                .setCancelable(false)
                .setPositiveButton("ابدأ", (dialog, which) -> {
                    String name = input.getText().toString().trim();

                    if (name.isEmpty()) {
                        name = "الطالب";
                    }

                    studentName = name;

                    prefs.edit()
                            .putString("student", studentName)
                            .apply();

                    showHome();
                })
                .show();
    }

    private void prepareRoot() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(18, 18, 18, 18);
        root.setBackgroundColor(Color.rgb(245, 249, 255));

        ScrollView scroll = new ScrollView(this);
        scroll.addView(root);

        setContentView(scroll);
    }

    private TextView title(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(27);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setTextColor(Color.rgb(25, 70, 120));
        t.setGravity(Gravity.CENTER);
        t.setPadding(8, 18, 8, 18);

        root.addView(t, new LinearLayout.LayoutParams(
                -1,
                -2
        ));

        return t;
    }

    private Button menuButton(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(19);
        b.setAllCaps(false);
        b.setPadding(10, 10, 10, 10);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, -2);

        p.setMargins(5, 7, 5, 7);

        root.addView(b, p);

        return b;
    }

    private void speak(String text) {
        if (tts != null) {
            tts.speak(
                    text,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "huroofna_" + System.currentTimeMillis()
            );
        }
    }

    private void addStar() {
        stars++;
        prefs.edit().putInt("stars", stars).apply();
    }

    private void showHome() {
        prepareRoot();

        title("🌟 حروفنا 🌟");

        TextView welcome = new TextView(this);
        welcome.setText(
                "أهلًا يا " + studentName + "\n" +
                "نجومك الحالية ⭐ " + stars
        );
        welcome.setTextSize(21);
        welcome.setGravity(Gravity.CENTER);
        welcome.setPadding(10, 5, 10, 20);

        root.addView(welcome);

        Button lettersBtn = menuButton("🔤 الحروف الهجائية");
        lettersBtn.setOnClickListener(v -> showLetters());

        Button groupsBtn = menuButton("📚 مجموعات الحروف");
        groupsBtn.setOnClickListener(v -> showGroups());

        Button movementsBtn = menuButton("َ ِ ُ الحركات");
        movementsBtn.setOnClickListener(v -> showMovements());

        Button maddBtn = menuButton("📏 المدود والمقاطع");
        maddBtn.setOnClickListener(v -> showMadd());

        Button wordsBtn = menuButton("🧩 الكلمات والصور");
        wordsBtn.setOnClickListener(v -> showWords());

        Button sentencesBtn = menuButton("📖 الجمل");
        sentencesBtn.setOnClickListener(v -> showSentences());

        Button understandingBtn = menuButton("🧠 تدريبات الفهم");
        understandingBtn.setOnClickListener(v -> showUnderstanding());

        Button numbersBtn = menuButton("🔢 الأرقام والحساب الأولي");
        numbersBtn.setOnClickListener(v -> showNumbers());

        Button gameBtn = menuButton("🎮 لعبة تمييز الحرف");
        gameBtn.setOnClickListener(v -> showGame());

        Button writingBtn = menuButton("✍️ تدريب الكتابة");
        writingBtn.setOnClickListener(v -> showWriting());

        Button progressBtn = menuButton("⭐ تقدّم الطالب");
        progressBtn.setOnClickListener(v -> showProgress());

        Button changeBtn = menuButton("👤 تغيير الطالب");
        changeBtn.setOnClickListener(v -> askStudentName());
    }

    private void showLetters() {
        prepareRoot();
        title("🔤 الحروف الهجائية");

        for (int i = 0; i < letters.length; i++) {
            final int index = i;

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setGravity(Gravity.CENTER_VERTICAL);
            card.setPadding(12, 12, 12, 12);

            TextView picture = new TextView(this);
            picture.setText(emoji[i]);
            picture.setTextSize(35);
            picture.setGravity(Gravity.CENTER);

            TextView letter = new TextView(this);
            letter.setText(
                    letters[i] + "\n" +
                    letterNames[i] + "\n" +
                    words[i]
            );
            letter.setTextSize(22);
            letter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            letter.setGravity(Gravity.CENTER);

            Button
