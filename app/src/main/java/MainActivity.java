package com.yemeni.huroofna;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private final String[] names = {
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

    private final String[] pictures = {
            "🦁","🦆","🍎","🦊","🐪","🐎","🐑","🐻",
            "🌽","🍎","🌸","🐟","☀️","🦅","🐸","✈️",
            "✉️","🍇","🦌","🐘","🌙","📖","🍋","🍌",
            "🐝","🌙","🌹","✋"
    };

    private final String[] numbers = {
            "١","٢","٣","٤","٥","٦","٧","٨","٩","١٠"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        prefs = getSharedPreferences("HuroofnaData", MODE_PRIVATE);

        studentName = prefs.getString("student", "");
        stars = prefs.getInt("stars", 0);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(new Locale("ar"));
                    tts.setSpeechRate(0.75f);
                }
            }
        });

        if (studentName.isEmpty()) {
            askStudent();
        } else {
            showHome();
        }
    }

    private void askStudent() {
        final EditText input = new EditText(this);
        input.setHint("اكتب اسم الطالب");
        input.setTextSize(20);
        input.setGravity(Gravity.CENTER);

        new AlertDialog.Builder(this)
                .setTitle("مرحبًا بك في حروفنا 🌟")
                .setMessage("اكتب اسم الطالب للبدء")
                .setView(input)
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
                .setCancelable(false)
                .show();
    }

    private void prepareRoot() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(20, 20, 20, 20);
        root.setBackgroundColor(Color.WHITE);

        setContentView(root);
    }

    private TextView title(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(27);
        tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.rgb(25, 80, 140));
        tv.setPadding(10, 20, 10, 20);

        root.addView(tv,
                new LinearLayout.LayoutParams(
                        -1,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

        return tv;
    }

    private Button button(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(19);
        b.setAllCaps(false);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, 65);

        p.setMargins(0, 7, 0, 7);

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

        prefs.edit()
                .putInt("stars", stars)
                .apply();
    }

    private void backButton() {
        Button b = button("⬅ العودة للرئيسية");
        b.setOnClickListener(v -> showHome());
    }

    private void showHome() {
        prepareRoot();

        title("🌟 حروفنا 🌟");

        TextView welcome = new TextView(this);
        welcome.setText(
                "أهلًا يا " + studentName +
                        "\nنجومك ⭐ " + stars
        );
        welcome.setTextSize(21);
        welcome.setGravity(Gravity.CENTER);
        welcome.setPadding(10, 10, 10, 20);

        root.addView(welcome);

        Button lettersButton = button("🔤 الحروف الهجائية");
        lettersButton.setOnClickListener(v -> showLetters());

        Button groupsButton = button("🧩 مجموعات الحروف");
        groupsButton.setOnClickListener(v -> showGroups());

        Button movementsButton = button("َ ِ ُ الحركات");
        movementsButton.setOnClickListener(v -> showMovements());

        Button maddButton = button("📏 المدود والمقاطع");
        maddButton.setOnClickListener(v -> showMadd());

        Button wordsButton = button("🧩 الكلمات والصور");
        wordsButton.setOnClickListener(v -> showWords());

        Button sentencesButton = button("📖 الجمل");
        sentencesButton.setOnClickListener(v -> showSentences());

        Button understandingButton = button("🧠 تدريبات الفهم");
        understandingButton.setOnClickListener(v -> showUnderstanding());

        Button numbersButton = button("🔢 الأرقام ١–١٠");
        numbersButton.setOnClickListener(v -> showNumbers());

        Button gameButton = button("🎮 لعبة تمييز الحرف");
        gameButton.setOnClickListener(v -> showGame());

        Button writingButton = button("✍️ تدريب الكتابة");
        writingButton.setOnClickListener(v -> showWriting());

        Button progressButton = button("⭐ تقدمي");
        progressButton.setOnClickListener(v -> showProgress());

        Button changeStudent = button("👤 تغيير الطالب");
        changeStudent.setOnClickListener(v -> {
            prefs.edit().remove("student").apply();
            studentName = "";
            askStudent();
        });
    }

    private void showLetters() {
        prepareRoot();
        title("🔤 الحروف الهجائية");

        for (int i = 0; i < letters.length; i++) {
            final int index = i;

            Button b = button(
                    letters[i] + "   —   " +
                            names[i] + "   " +
                            pictures[i]
            );

            b.setOnClickListener(v -> {
                speak(names[index]);
                addStar();
            });
        }

        backButton();
    }

    private void showGroups() {
        prepareRoot();
        title("🧩 مجموعات الحروف");

        Button lip = button("👅 الحروف اللثوية");
        lip.setOnClickListener(v ->
                showGroup("الحروف اللثوية",
                        new String[]{"ث","ذ","ظ"}));

        Button raf = button("🔗 الحروف الرافسة");
        raf.setOnClickListener(v ->
                showGroup("الحروف الرافسة",
                        new String[]{"ا","د","ذ","ر","ز","و"}));

        Button similar = button("🔎 الحروف المتشابهة");
        similar.setOnClickListener(v -> showSimilar());

        Button strong = button("💪 الحروف القوية");
        strong.setOnClickListener(v ->
                showGroup("الحروف القوية",
                        new String[]{"ص","ض","ط","ظ"}));

        backButton();
    }

    private void showGroup(String titleText, String[] group) {
        prepareRoot();
        title(titleText);

        for (String letter : group) {
            Button b = button("🔤 " + letter);
            b.setOnClickListener(v -> {
                speak(letter);
                addStar();
            });
        }

        backButton();
    }

    private void showSimilar() {
        prepareRoot();
        title("🔎 الحروف المتشابهة");

        String[] groups = {
                "ب   ت   ث",
                "ج   ح   خ",
                "د   ذ",
                "ر   ز",
                "س   ش",
                "ص   ض",
                "ط   ظ",
                "ع   غ",
                "ف   ق"
        };

        for (String g : groups) {
            Button b = button(g);
            b.setOnClickListener(v -> {
                speak(g);
                addStar();
            });
        }

        backButton();
    }

    private void showMovements() {
        prepareRoot();
        title("َ ِ ُ الحركات");

        Button fatha = button("بَ  —  فتحة");
        fatha.setOnClickListener(v -> {
            speak("بَ");
            addStar();
        });

        Button kasra = button("بِ  —  كسرة");
        kasra.setOnClickListener(v -> {
            speak("بِ");
            addStar();
        });

        Button damma = button("بُ  —  ضمة");
        damma.setOnClickListener(v -> {
            speak("بُ");
            addStar();
        });

        backButton();
    }

    private void showMadd() {
        prepareRoot();
        title("📏 المدود والمقاطع");

        String[] syllables = {
                "با","بو","بي",
                "ما","مو","مي",
                "سا","سو","سي",
                "لا","لو","لي"
        };

        for (String s : syllables) {
            Button b = button(s);
            b.setOnClickListener(v -> {
                speak(s);
                addStar();
            });
        }

        backButton();
    }

    private void showWords() {
        prepareRoot();
        title("🧩 الكلمات والصور");

        for (int i = 0; i < words.length; i++) {
            final int index = i;

            Button b = button(
                    pictures[i] + "   " +
                            words[i]
            );

            b.setOnClickListener(v -> {
                speak(words[index]);
                addStar();
            });
        }

        backButton();
    }

    private void showSentences() {
        prepareRoot();
        title("📖 الجمل");

        String[] sentences = {
                "هذا باب.",
                "ماما تقرأ.",
                "أنا أحب المدرسة.",
                "هذا قلم.",
                "هذا كتاب.",
                "أحب أبي وأمي."
        };

        for (String s : sentences) {
            Button b = button(s);
            b.setOnClickListener(v -> {
                speak(s);
                addStar();
            });
        }

        backButton();
    }

    private void showUnderstanding() {
        prepareRoot();
        title("🧠 تدريب الفهم");

        TextView q = new TextView(this);
        q.setText(
                "أين القلم؟ ✏️\n\n" +
                        "اختر الإجابة الصحيحة:"
        );
        q.setTextSize(21);
        q.setGravity(Gravity.CENTER);
        root.addView(q);

        Button a1 = button("📖 الكتاب");
        a1.setOnClickListener(v -> speak("الكتاب"));

        Button a2 = button("✏️ القلم");
        a2.setOnClickListener(v -> {
            speak("القلم");
            addStar();

            new AlertDialog.Builder(this)
                    .setTitle("أحسنت ⭐")
                    .setMessage("إجابة صحيحة")
                    .setPositiveButton("ممتاز", null)
                    .show();
        });

        Button a3 = button("🍎 التفاحة");
        a3.setOnClickListener(v -> speak("التفاحة"));

        backButton();
    }

    private void showNumbers() {
        prepareRoot();
        title("🔢 الأرقام من ١ إلى ١٠");

        for (int i = 0; i < numbers.length; i++) {
            final int index = i;

            StringBuilder objects = new StringBuilder();

            for (int j = 0; j <= i; j++) {
                objects.append("🍎");
            }

            Button b = button(
                    numbers[i] + "     " +
                            objects.toString()
            );

            b.setOnClickListener(v -> {
                speak(numbers[index]);
                addStar();
            });
        }

        Button count = button("🔢 تدريب العد");
        count.setOnClickListener(v -> showCounting());

        Button addition = button("➕ الجمع البسيط");
        addition.setOnClickListener(v -> showAddition());

        Button subtraction = button("➖ الطرح البسيط");
        subtraction.setOnClickListener(v -> showSubtraction());

        backButton();
    }

    private void showCounting() {
        prepareRoot();
        title("🔢 تدريب العد");

        TextView t = new TextView(this);
        t.setText("🍎 🍎 🍎 🍎 🍎\n\nكم تفاحة؟");
        t.setTextSize(26);
        t.setGravity(Gravity.CENTER);

        root.addView(t);

        Button correct = button("٥");
        correct.setOnClickListener(v -> {
            addStar();
            speak("خمسة");

            new AlertDialog.Builder(this)
                    .setTitle("أحسنت ⭐")
                    .setMessage("الإجابة صحيحة")
                    .setPositiveButton("ممتاز", null)
                    .show();
        });

        Button wrong = button("٣");
        wrong.setOnClickListener(v -> speak("حاول مرة أخرى"));

        backButton();
    }

    private void showAddition() {
        prepareRoot();
        title("➕ الجمع");

        TextView t = new TextView(this);
        t.setText("🍎 🍎  +  🍎 = ؟");
        t.setTextSize(28);
        t.setGravity(Gravity.CENTER);
        root.addView(t);

        Button correct = button("٣");
        correct.setOnClickListener(v -> {
            addStar();
            speak("ثلاثة");
        });

        Button wrong = button("٢");
        wrong.setOnClickListener(v -> speak("حاول مرة أخرى"));

        backButton();
    }

    private void showSubtraction() {
        prepareRoot();
        title("➖ الطرح");

        TextView t = new TextView(this);
        t.setText("🍎 🍎 🍎  −  🍎 = ؟");
        t.setTextSize(28);
        t.setGravity(Gravity.CENTER);
        root.addView(t);

        Button correct = button("٢");
        correct.setOnClickListener(v -> {
            addStar();
            speak("اثنان");
        });

        Button wrong = button("١");
        wrong.setOnClickListener(v -> speak("حاول مرة أخرى"));

        backButton();
    }

    private void showGame() {
        prepareRoot();
        title("🎮 لعبة تمييز الحرف");

        TextView question = new TextView(this);
        question.setText("أين حرف ب ؟");
        question.setTextSize(27);
        question.setGravity(Gravity.CENTER);
        root.addView(question);

        Button a = button("ت");
        a.setOnClickListener(v -> speak("حاول مرة أخرى"));

        Button b = button("ب");
        b.setOnClickListener(v -> {
            addStar();
            speak("ب");

            new AlertDialog.Builder(this)
                    .setTitle("🎉 أحسنت!")
                    .setMessage("إجابة صحيحة ⭐")
                    .setPositiveButton("رائع", null)
                    .show();
        });

        Button c = button("ث");
        c.setOnClickListener(v -> speak("حاول مرة أخرى"));

        backButton();
    }

    private void showWriting() {
        prepareRoot();
        title("✍️ تدريب الكتابة");

        TextView example = new TextView(this);
        example.setText("اكتب الحرف:\n\nا");
        example.setTextSize(32);
        example.setGravity(Gravity.CENTER);
        root.addView(example);

        EditText input = new EditText(this);
        input.setTextSize(32);
        input.setGravity(Gravity.CENTER);
        input.setHint("اكتب هنا");

        root.addView(
                input,
                new LinearLayout.LayoutParams(-1, 100)
        );

        Button check = button("✅ تحقق");
        check.setOnClickListener(v -> {
            String answer = input.getText().toString().trim();

            if (answer.equals("ا")) {
                addStar();
                speak("أحسنت");

                new AlertDialog.Builder(this)
                        .setTitle("أحسنت ⭐")
                        .setMessage("كتابة صحيحة")
                        .setPositiveButton("ممتاز", null)
                        .show();
            } else {
                speak("حاول مرة أخرى");
            }
        });

        backButton();
    }

    private void showProgress() {
        prepareRoot();
        title("⭐ تقدمي");

        TextView p = new TextView(this);

        p.setText(
                "الطالب: " + studentName +
                        "\n\nالنجوم: ⭐ " + stars +
                        "\n\nاستمر في التعلم والتدريب 🌟"
        );

        p.setTextSize(24);
        p.setGravity(Gravity.CENTER);
        p.setPadding(10, 30, 10, 30);

        root.addView(p);

        Button reset = button("🔄 تصفير النجوم");

        reset.setOnClickListener(v -> {
            stars = 0;

            prefs.edit()
                    .putInt("stars", 0)
                    .apply();

            showProgress();
        });

        backButton();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }
}
