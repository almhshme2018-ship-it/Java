package com.yemeni.huroofna;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    LinearLayout mainLayout;
    SharedPreferences prefs;
    String studentName;
    int stars;

    String[] letters = {
            "ا", "ب", "ت", "ث", "ج", "ح", "خ",
            "د", "ذ", "ر", "ز", "س", "ش", "ص",
            "ض", "ط", "ظ", "ع", "غ", "ف", "ق",
            "ك", "ل", "م", "ن", "ه", "و", "ي"
    };

    String[] letterNames = {
            "ألف", "باء", "تاء", "ثاء", "جيم", "حاء", "خاء",
            "دال", "ذال", "راء", "زاي", "سين", "شين", "صاد",
            "ضاد", "طاء", "ظاء", "عين", "غين", "فاء", "قاف",
            "كاف", "لام", "ميم", "نون", "هاء", "واو", "ياء"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        prefs = getSharedPreferences("Huroofna", Context.MODE_PRIVATE);
        studentName = prefs.getString("student", "");

        stars = prefs.getInt("stars", 0);

        if (studentName.isEmpty()) {
            showStudentDialog();
        } else {
            showHome();
        }
    }

    private void showStudentDialog() {

        final EditText input = new EditText(this);
        input.setHint("اكتب اسم الطالب");
        input.setTextSize(20);
        input.setGravity(Gravity.CENTER);
        input.setPadding(20, 20, 20, 20);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("👦 مرحبًا بك في حروفنا")
                .setMessage("أدخل اسم الطالب للبدء")
                .setView(input)
                .setCancelable(false)
                .setPositiveButton("بدء التعلم", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {

                        String name = input.getText().toString().trim();

                        if (name.isEmpty()) {
                            input.setError("اكتب اسم الطالب");
                            return;
                        }

                        studentName = name;
                        stars = 0;

                        prefs.edit()
                                .putString("student", studentName)
                                .putInt("stars", stars)
                                .apply();

                        dialog.dismiss();
                        showHome();
                    });
        });

        dialog.show();
    }

    private void showHome() {

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mainLayout.setPadding(20, 20, 20, 20);
        mainLayout.setBackgroundColor(Color.rgb(248, 250, 255));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.addView(mainLayout);

        setContentView(scroll);

        TextView title = textView(
                "حروفنا",
                34,
                Color.rgb(25, 95, 180)
        );
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        mainLayout.addView(title);

        TextView welcome = textView(
                "مرحبًا يا " + studentName + " 🌟",
                23,
                Color.DKGRAY
        );
        mainLayout.addView(welcome);

        TextView starView = textView(
                "⭐ نجومك: " + stars,
                21,
                Color.rgb(230, 150, 0)
        );
        mainLayout.addView(starView);

        addSpace();

        Button lettersButton = menuButton("🔤 الحروف الهجائية");
        lettersButton.setOnClickListener(v -> showLetters());
        mainLayout.addView(lettersButton);

        Button soundButton = menuButton("🔊 نطق الحروف");
        soundButton.setOnClickListener(v -> showLetters());
        mainLayout.addView(soundButton);

        Button movementsButton = menuButton("َ ِ ُ  الحركات");
        movementsButton.setOnClickListener(v -> showMovements());
        mainLayout.addView(movementsButton);

        Button maddButton = menuButton("📏 المدود والمقاطع الأساسية");
        maddButton.setOnClickListener(v -> showMadd());
        mainLayout.addView(maddButton);

        Button wordsButton = menuButton("🧩 الكلمات");
        wordsButton.setOnClickListener(v -> showWords());
        mainLayout.addView(wordsButton);

        Button sentencesButton = menuButton("📖 الجمل");
        sentencesButton.setOnClickListener(v -> showSentences());
        mainLayout.addView(sentencesButton);

        Button understandingButton = menuButton("🧠 تدريبات الفهم");
        understandingButton.setOnClickListener(v -> showUnderstanding());
        mainLayout.addView(understandingButton);

        Button gameButton = menuButton("🎮 لعبة تمييز الحرف");
        gameButton.setOnClickListener(v -> showGame());
        mainLayout.addView(gameButton);

        Button writingButton = menuButton("✍️ تدريب الكتابة");
        writingButton.setOnClickListener(v -> showWriting());
        mainLayout.addView(writingButton);

        Button progressButton = menuButton("⭐ التقدم والنجوم");
        progressButton.setOnClickListener(v -> showProgress());
        mainLayout.addView(progressButton);

        addSpace();

        Button changeStudent = menuButton("👤 تغيير الطالب");
        changeStudent.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            studentName = "";
            stars = 0;
            showStudentDialog();
        });

        mainLayout.addView(changeStudent);
    }

    private void showLetters() {

        clearPage("🔤 الحروف الهجائية");

        for (int i = 0; i < letters.length; i++) {

            final int index = i;

            Button b = menuButton(
                    letters[i] + "   " + letterNames[i]
            );

            b.setOnClickListener(v -> {

                stars++;
                prefs.edit().putInt("stars", stars).apply();

                new AlertDialog.Builder(this)
                        .setTitle("الحرف " + letters[index])
                        .setMessage(
                                "اسم الحرف: " + letterNames[index] +
                                "\n\nأحسنت يا " + studentName + "! ⭐"
                        )
                        .setPositiveButton("التالي", null)
                        .show();
            });

            mainLayout.addView(b);
        }

        addBackButton();
    }

    private void showMovements() {

        clearPage("َ ِ ُ الحركات");

        addLesson(
                "الفتحة",
                "بَ",
                "بَاء"
        );

        addLesson(
                "الكسرة",
                "بِ",
                "بِاء"
        );

        addLesson(
                "الضمة",
                "بُ",
                "بُاء"
        );

        addBackButton();
    }

    private void showMadd() {

        clearPage("📏 المدود والمقاطع");

        addLesson("المد بالألف", "بَا", "بَاب");
        addLesson("المد بالواو", "بُو", "نُور");
        addLesson("المد بالياء", "بِي", "فِيل");

        addBackButton();
    }

    private void showWords() {

        clearPage("🧩 الكلمات");

        addLesson("كلمة", "بَاب", "باب");
        addLesson("كلمة", "كِتَاب", "كتاب");
        addLesson("كلمة", "قَلَم", "قلم");
        addLesson("كلمة", "مَوْز", "موز");

        addBackButton();
    }

    private void showSentences() {

        clearPage("📖 الجمل");

        addLesson("جملة", "هَذَا كِتَابٌ.", "اقرأ الجملة");
        addLesson("جملة", "هَذَا قَلَمٌ.", "اقرأ الجملة");
        addLesson("جملة", "أَكَلَ أَحْمَدُ التُّفَّاحَةَ.", "اقرأ الجملة");

        addBackButton();
    }

    private void showUnderstanding() {

        clearPage("🧠 تدريبات الفهم");

        TextView q = textView(
                "ما لون التفاحة؟",
                25,
                Color.DKGRAY
        );

        mainLayout.addView(q);

        String[] answers = {"أحمر", "أزرق", "أخضر"};

        for (String answer : answers) {

            Button b = menuButton(answer);

            b.setOnClickListener(v -> {

                if (answer.equals("أحمر")) {
                    stars++;
                    prefs.edit().putInt("stars", stars).apply();

                    Toast.makeText(
                            this,
                            "أحسنت! ⭐ +1",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            this,
                            "حاول مرة أخرى",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

            mainLayout.addView(b);
        }

        addBackButton();
    }

    private void showGame() {

        clearPage("🎮 لعبة تمييز الحرف");

        TextView question = textView(
                "اختر الحرف: ب",
                30,
                Color.rgb(30, 80, 160)
        );

        mainLayout.addView(question);

        String[] options = {"ت", "ب", "ث", "ن"};

        for (String option : options) {

            Button b = menuButton(option);

            b.setOnClickListener(v -> {

                if (option.equals("ب")) {

                    stars += 2;
                    prefs.edit().putInt("stars", stars).apply();

                    Toast.makeText(
                            this,
                            "رائع! ⭐⭐ +2",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    Toast.makeText(
                            this,
                            "حاول مرة أخرى",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

            mainLayout.addView(b);
        }

        addBackButton();
    }

    private void showWriting() {

        clearPage("✍️ تدريب الكتابة");

        TextView instruction = textView(
                "تدرّب على كتابة الحرف",
                24,
                Color.DKGRAY
        );

        mainLayout.addView(instruction);

        EditText writing = new EditText(this);
        writing.setText("ا");
        writing.setTextSize(70);
        writing.setGravity(Gravity.CENTER);
        writing.setHint("اكتب الحرف هنا");

        mainLayout.addView(
                writing,
                new LinearLayout.LayoutParams(
                        -1,
                        220
                )
        );

        Button done = menuButton("⭐ أنهيت التدريب");

        done.setOnClickListener(v -> {

            stars++;
            prefs.edit().putInt("stars", stars).apply();

            Toast.makeText(
                    this,
                    "أحسنت! ⭐ +1",
                    Toast.LENGTH_SHORT
            ).show();
        });

        mainLayout.addView(done);

        addBackButton();
    }

    private void showProgress() {

        clearPage("⭐ التقدم والنجوم");

        TextView progress = textView(
                "الطالب: " + studentName +
                        "\n\n⭐ مجموع النجوم: " + stars +
                        "\n\n🔤 الحروف: 28 حرفًا" +
                        "\n📚 الكلمات والجمل" +
                        "\n🎮 الألعاب والتدريبات" +
                        "\n✍️ الكتابة",
                23,
                Color.DKGRAY
        );

        mainLayout.addView(progress);

        Button reset = menuButton("إعادة النجوم إلى الصفر");

        reset.setOnClickListener(v -> {

            stars = 0;
            prefs.edit().putInt("stars", 0).apply();

            showProgress();
        });

        mainLayout.addView(reset);

        addBackButton();
    }

    private void addLesson(
            String title,
            String example,
            String description
    ) {

        TextView t = textView(
                title + "\n\n" + example +
                        "\n" + description,
                26,
                Color.DKGRAY
        );

        t.setPadding(20, 30, 20, 30);

        mainLayout.addView(t);
    }

    private Button menuButton(String text) {

        Button b = new Button(this);

        b.setText(text);
        b.setTextSize(20);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);
        b.setPadding(15, 20, 15, 20);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        p.setMargins(5, 8, 5, 8);

        b.setLayoutParams(p);

        return b;
    }

    private TextView textView(
            String text,
            int size,
            int color
    ) {

        TextView t = new TextView(this);

        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        t.setPadding(10, 15, 10, 15);
        t.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        return t;
    }

    private void clearPage(String title) {

        mainLayout.removeAllViews();

        TextView t = textView(
                title,
                30,
                Color.rgb(25, 95, 180)
        );

        t.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        mainLayout.addView(t);

        addSpace();
    }

    private void addBackButton() {

        Button back = menuButton("⬅️ العودة للرئيسية");

        back.setOnClickListener(v -> showHome());

        mainLayout.addView(back);
    }

    private void addSpace() {

        Space space = new Space(this);

        mainLayout.addView(
                space,
                new LinearLayout.LayoutParams(
                        1,
                        15
                )
        );
    }
}
