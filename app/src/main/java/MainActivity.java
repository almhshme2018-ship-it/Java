package com.yemeni.huroofna;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
            "ص","ض","ط","ظ","ع","غ","ف","ق","ك","ل","م","ن","ه","و","ي"
    };

    private final String[] names = {
            "ألف","باء","تاء","ثاء","جيم","حاء","خاء","دال","ذال",
            "راء","زاي","سين","شين","صاد","ضاد","طاء","ظاء","عين",
            "غين","فاء","قاف","كاف","لام","ميم","نون","هاء","واو","ياء"
    };

    private final String[] words = {
            "أسد","بطة","تفاحة","ثعلب","جمل","حوت","خروف","دب",
            "ذرة","رمان","زهرة","سمكة","شجرة","صقر","ضفدع","طائرة",
            "ظبي","عصفور","غزال","فراشة","قلم","كتاب","ليمون","موز",
            "نحلة","هلال","وردة","يد"
    };

    private final String[] pictures = {
            "🦁","🦆","🍎","🦊","🐪","🐋","🐑","🐻",
            "🌽","🍎","🌸","🐟","🌳","🦅","🐸","✈️",
            "🦌","🐦","🦌","🦋","✏️","📖","🍋","🍌",
            "🐝","🌙","🌹","✋"
    };

    private final String[] numbers = {
            "١","٢","٣","٤","٥","٦","٧","٨","٩","١٠"
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
                int result = tts.setLanguage(new Locale("ar"));
                tts.setSpeechRate(0.75f);
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // The device does not currently provide Arabic TTS.
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
        input.setSingleLine(true);

        new AlertDialog.Builder(this)
                .setTitle("مرحبًا بك في حروفنا 🌟")
                .setMessage("اكتب اسم الطالب للبدء")
                .setView(input)
                .setPositiveButton("ابدأ", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) name = "الطالب";

                    studentName = name;
                    prefs.edit().putString("student", studentName).apply();
                    showHome();
                })
                .setCancelable(false)
                .show();
    }

    private void prepareRoot() {
        LinearLayout outer = new LinearLayout(this);
        outer.setOrientation(LinearLayout.VERTICAL);
        outer.setBackgroundColor(Color.rgb(246, 251, 255));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(246, 251, 255));

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(10), dp(10), dp(10), dp(18));
        scroll.addView(root);

        outer.addView(scroll, new LinearLayout.LayoutParams(
                -1, 0, 1f));

        LinearLayout nav = new LinearLayout(this);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(dp(4), dp(3), dp(4), dp(3));
        GradientDrawable navBg = new GradientDrawable();
        navBg.setColor(Color.WHITE);
        navBg.setStroke(dp(1), Color.rgb(220, 232, 244));
        nav.setBackground(navBg);

        Button home = navButton("⌂\nالرئيسية");
        Button lettersNav = navButton("🔤\nالحروف");
        Button gamesNav = navButton("🎮\nالألعاب");
        Button progressNav = navButton("⭐\nالتقدم");
        home.setOnClickListener(v -> showHome());
        lettersNav.setOnClickListener(v -> showLetters());
        gamesNav.setOnClickListener(v -> showGame());
        progressNav.setOnClickListener(v -> showProgress());
        nav.addView(home, new LinearLayout.LayoutParams(0, dp(62), 1f));
        nav.addView(lettersNav, new LinearLayout.LayoutParams(0, dp(62), 1f));
        nav.addView(gamesNav, new LinearLayout.LayoutParams(0, dp(62), 1f));
        nav.addView(progressNav, new LinearLayout.LayoutParams(0, dp(62), 1f));

        outer.addView(nav, new LinearLayout.LayoutParams(-1, dp(68)));
        setContentView(outer);
    }

    private Button navButton(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(12);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);
        b.setTextColor(Color.rgb(25, 90, 160));
        b.setPadding(2, 0, 2, 0);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.WHITE);
        bg.setCornerRadius(dp(18));
        b.setBackground(bg);
        return b;
    }

    private TextView title(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(27);
        tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.rgb(20, 72, 125));
        tv.setPadding(10, 18, 10, 18);

        root.addView(tv, new LinearLayout.LayoutParams(
                -1, LinearLayout.LayoutParams.WRAP_CONTENT));
        return tv;
    }

    private Button button(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(19);
        b.setAllCaps(false);
        b.setTextColor(Color.rgb(35, 55, 75));
        b.setGravity(Gravity.CENTER);
        b.setPadding(10, 4, 10, 4);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.WHITE);
        bg.setCornerRadius(28);
        bg.setStroke(2, Color.rgb(210, 225, 240));
        b.setBackground(bg);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, 62);
        p.setMargins(0, 6, 0, 6);
        root.addView(b, p);
        return b;
    }

    private Button controlButton(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(17);
        b.setAllCaps(false);
        b.setTextColor(Color.rgb(35, 55, 75));
        b.setGravity(Gravity.CENTER);
        b.setPadding(6, 4, 6, 4);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.WHITE);
        bg.setCornerRadius(28);
        bg.setStroke(2, Color.rgb(210, 225, 240));
        b.setBackground(bg);
        return b;
    }

    private Button homeCard(String icon, String text, int backgroundColor) {
        Button b = new Button(this);
        b.setText(icon + "\n" + text);
        b.setTextSize(16);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);
        b.setTextColor(Color.rgb(35, 55, 75));
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setPadding(dp(4), dp(7), dp(4), dp(7));

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(backgroundColor);
        bg.setCornerRadius(dp(24));
        bg.setStroke(dp(2), Color.WHITE);
        b.setBackground(bg);

        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width = 0;
        lp.height = dp(112);
        lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        lp.setMargins(dp(4), dp(4), dp(4), dp(4));
        b.setLayoutParams(lp);
        return b;
    }

    private TextView homeHeader() {
        TextView header = new TextView(this);
        header.setText("📚  حروفنا  ✏️\n\nمرحبًا يا " + studentName +
                "\n🎙️ معلمنا أ/ هشام    ⭐ " + stars);
        header.setTextSize(21);
        header.setGravity(Gravity.CENTER);
        header.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        header.setTextColor(Color.WHITE);
        header.setPadding(dp(12), dp(18), dp(12), dp(18));

        GradientDrawable bg = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.rgb(22, 151, 232), Color.rgb(31, 103, 194)});
        bg.setCornerRadius(dp(28));
        header.setBackground(bg);

        root.addView(header, new LinearLayout.LayoutParams(
                -1, LinearLayout.LayoutParams.WRAP_CONTENT));
        return header;
    }

    private TextView sectionTitle(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(19);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tv.setTextColor(Color.rgb(30, 80, 125));
        tv.setPadding(8, 16, 8, 8);
        root.addView(tv);
        return tv;
    }

    private void speak(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,
                    "huroofna_" + System.currentTimeMillis());
        }
    }

    private void addStar() {
        stars++;
        prefs.edit().putInt("stars", stars).apply();
    }

    private void backButton() {
        Button b = button("⬅ العودة للرئيسية");
        b.setOnClickListener(v -> showHome());
    }

    private void showHome() {
        prepareRoot();
        homeHeader();

        TextView hint = new TextView(this);
        hint.setText("🌈 هيا نتعلم ونلعب ونردد الحروف معًا!");
        hint.setTextSize(17);
        hint.setGravity(Gravity.CENTER);
        hint.setTextColor(Color.rgb(55, 90, 120));
        hint.setPadding(dp(6), dp(10), dp(6), dp(6));
        root.addView(hint);

        Button songButton = homeCard("🎵", "أنشودة الحروف الهجائية", Color.rgb(255, 220, 92));
        songButton.setTextSize(17);
        songButton.setOnClickListener(v -> showAlphabetSong());
        LinearLayout.LayoutParams songLp = new LinearLayout.LayoutParams(-1, dp(72));
        songLp.setMargins(dp(4), dp(6), dp(4), dp(8));
        root.addView(songButton, songLp);

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(2);
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

        int[] colors = {
                Color.rgb(255, 235, 130), Color.rgb(183, 229, 255),
                Color.rgb(202, 242, 187), Color.rgb(255, 204, 218),
                Color.rgb(216, 203, 255), Color.rgb(255, 220, 174)
        };

        Button lettersButton = homeCard("🔤", "الحروف الهجائية", colors[0]);
        lettersButton.setOnClickListener(v -> showLetters());
        grid.addView(lettersButton);

        Button groupsButton = homeCard("🧩", "مجموعات الحروف", colors[1]);
        groupsButton.setOnClickListener(v -> showGroups());
        grid.addView(groupsButton);

        Button movementsButton = homeCard("َ ِ ُ", "الحركات", colors[2]);
        movementsButton.setOnClickListener(v -> showMovements());
        grid.addView(movementsButton);

        Button maddButton = homeCard("📏", "المدود والمقاطع", colors[3]);
        maddButton.setOnClickListener(v -> showMadd());
        grid.addView(maddButton);

        Button wordsButton = homeCard("🖼️", "الكلمات والصور", colors[4]);
        wordsButton.setOnClickListener(v -> showWords());
        grid.addView(wordsButton);

        Button sentencesButton = homeCard("📖", "الجمل", colors[5]);
        sentencesButton.setOnClickListener(v -> showSentences());
        grid.addView(sentencesButton);

        Button understandingButton = homeCard("🧠", "تدريبات الفهم", colors[0]);
        understandingButton.setOnClickListener(v -> showUnderstanding());
        grid.addView(understandingButton);

        Button numbersButton = homeCard("🔢", "الأرقام ١–١٠", colors[1]);
        numbersButton.setOnClickListener(v -> showNumbers());
        grid.addView(numbersButton);

        Button gameButton = homeCard("🎮", "لعبة تمييز الحرف", colors[2]);
        gameButton.setOnClickListener(v -> showGame());
        grid.addView(gameButton);

        Button writingButton = homeCard("✍️", "تدريب الكتابة", colors[3]);
        writingButton.setOnClickListener(v -> showWriting());
        grid.addView(writingButton);

        Button progressButton = homeCard("⭐", "تقدمي والنجوم", colors[4]);
        progressButton.setOnClickListener(v -> showProgress());
        grid.addView(progressButton);

        Button changeStudent = homeCard("👤", "تغيير الطالب", colors[5]);
        changeStudent.setOnClickListener(v -> {
            prefs.edit().remove("student").apply();
            studentName = "";
            askStudent();
        });
        grid.addView(changeStudent);

        root.addView(grid, new LinearLayout.LayoutParams(-1, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView footer = new TextView(this);
        footer.setText("🌱 معًا نحو القراءة والكتابة والنجاح ⭐");
        footer.setTextSize(16);
        footer.setGravity(Gravity.CENTER);
        footer.setTextColor(Color.rgb(45, 110, 75));
        footer.setPadding(dp(8), dp(14), dp(8), dp(10));
        root.addView(footer);
    }

    private void showAlphabetSong() {
        prepareRoot();

        TextView top = new TextView(this);
        top.setText("🎵 أنشودة الحروف الهجائية");
        top.setTextSize(24);
        top.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        top.setGravity(Gravity.CENTER);
        top.setTextColor(Color.WHITE);
        top.setPadding(dp(10), dp(14), dp(10), dp(14));
        GradientDrawable topBg = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.rgb(19, 151, 232), Color.rgb(40, 94, 202)});
        topBg.setCornerRadius(dp(24));
        top.setBackground(topBg);
        root.addView(top, new LinearLayout.LayoutParams(-1, dp(66)));

        TextView teacher = new TextView(this);
        teacher.setText("🎙️ معلمنا أ/ هشام\nهيا نتعلم الحروف ونرددها معًا!");
        teacher.setTextSize(19);
        teacher.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        teacher.setGravity(Gravity.CENTER);
        teacher.setTextColor(Color.rgb(30, 75, 130));
        teacher.setPadding(dp(8), dp(10), dp(8), dp(10));
        root.addView(teacher, new LinearLayout.LayoutParams(-1, dp(82)));

        final TextView progress = new TextView(this);
        progress.setText("1 / 28");
        progress.setTextSize(16);
        progress.setGravity(Gravity.CENTER);
        progress.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(progress, new LinearLayout.LayoutParams(-1, dp(38)));

        final TextView bigLetter = new TextView(this);
        bigLetter.setText("ا");
        bigLetter.setTextSize(78);
        bigLetter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        bigLetter.setGravity(Gravity.CENTER);
        bigLetter.setTextColor(Color.rgb(232, 38, 50));
        root.addView(bigLetter, new LinearLayout.LayoutParams(-1, dp(105)));

        final TextView picture = new TextView(this);
        picture.setText("🦁");
        picture.setTextSize(58);
        picture.setGravity(Gravity.CENTER);
        root.addView(picture, new LinearLayout.LayoutParams(-1, dp(82)));

        final TextView word = new TextView(this);
        word.setText("أسد");
        word.setTextSize(30);
        word.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        word.setGravity(Gravity.CENTER);
        word.setTextColor(Color.rgb(25, 35, 50));
        root.addView(word, new LinearLayout.LayoutParams(-1, dp(54)));

        Button listen = homeCard("🔊", "استمع", Color.rgb(71, 151, 240));
        listen.setTextColor(Color.WHITE);
        root.addView(listen, new LinearLayout.LayoutParams(-1, dp(64)));

        LinearLayout controls = new LinearLayout(this);
        controls.setGravity(Gravity.CENTER);
        controls.setOrientation(LinearLayout.HORIZONTAL);
        Button prev = controlButton("◀ السابق");
        Button play = controlButton("▶ تشغيل");
        Button next = controlButton("التالي ▶");
        controls.addView(prev, new LinearLayout.LayoutParams(0, dp(58), 1f));
        controls.addView(play, new LinearLayout.LayoutParams(0, dp(58), 1f));
        controls.addView(next, new LinearLayout.LayoutParams(0, dp(58), 1f));
        root.addView(controls, new LinearLayout.LayoutParams(-1, dp(68)));

        final TextView hint = new TextView(this);
        hint.setText("🎙️ معلمنا أ/ هشام — اسمع ثم ردد");
        hint.setTextSize(17);
        hint.setGravity(Gravity.CENTER);
        hint.setTextColor(Color.rgb(35, 80, 125));
        root.addView(hint, new LinearLayout.LayoutParams(-1, dp(55)));

        Button back = controlButton("⬅ العودة للرئيسية");
        back.setOnClickListener(v -> showHome());
        root.addView(back, new LinearLayout.LayoutParams(-1, dp(58)));

        final int[] current = {0};
        final Handler handler = new Handler(Looper.getMainLooper());
        final boolean[] running = {false};

        Runnable[] step = new Runnable[1];
        step[0] = () -> {
            if (!running[0]) return;
            if (current[0] >= letters.length) {
                running[0] = false;
                addStar();
                hint.setText("🎉 أحسنت! أكملت أنشودة الحروف ⭐");
                progress.setText("28 / 28   ⭐");
                return;
            }
            int i = current[0];
            bigLetter.setText(letters[i]);
            picture.setText(pictures[i]);
            word.setText(words[i]);
            progress.setText((i + 1) + " / 28");
            hint.setText("🎙️ معلمنا أ/ هشام — ردد: " + names[i]);
            speak(names[i] + ". " + words[i]);
            current[0]++;
            handler.postDelayed(step[0], 1900);
        };

        Runnable showCurrent = () -> {
            int i = Math.max(0, Math.min(current[0], letters.length - 1));
            bigLetter.setText(letters[i]);
            picture.setText(pictures[i]);
            word.setText(words[i]);
            progress.setText((i + 1) + " / 28");
        };

        listen.setOnClickListener(v -> {
            int i = Math.max(0, Math.min(current[0] - 1, letters.length - 1));
            speak(names[i] + ". " + words[i]);
        });
        play.setOnClickListener(v -> {
            if (!running[0]) {
                running[0] = true;
                if (current[0] >= letters.length) current[0] = 0;
                step[0].run();
            }
        });
        prev.setOnClickListener(v -> {
            running[0] = false;
            handler.removeCallbacks(step[0]);
            current[0] = Math.max(0, current[0] - 1);
            showCurrent.run();
            speak(names[current[0]] + ". " + words[current[0]]);
        });
        next.setOnClickListener(v -> {
            running[0] = false;
            handler.removeCallbacks(step[0]);
            current[0] = Math.min(letters.length - 1, current[0] + 1);
            showCurrent.run();
            speak(names[current[0]] + ". " + words[current[0]]);
        });
    }

    private void showLetters() {
        prepareRoot();
        title("🔤 الحروف الهجائية");

        for (int i = 0; i < letters.length; i++) {
            final int index = i;
            Button b = button(letters[i] + "   —   " + names[i] + "   " + pictures[i]);
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
        lip.setOnClickListener(v -> showGroup("الحروف اللثوية",
                new String[]{"ث","ذ","ظ"}));

        Button raf = button("🔗 الحروف الرافسة");
        raf.setOnClickListener(v -> showGroup("الحروف الرافسة",
                new String[]{"ا","د","ذ","ر","ز","و"}));

        Button similar = button("🔎 الحروف المتشابهة");
        similar.setOnClickListener(v -> showSimilar());

        Button strong = button("💪 الحروف القوية");
        strong.setOnClickListener(v -> showGroup("الحروف القوية",
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
                "ب   ت   ث","ج   ح   خ","د   ذ","ر   ز","س   ش",
                "ص   ض","ط   ظ","ع   غ","ف   ق"
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

        Button fatha = button("بَ   —   فتحة");
        fatha.setOnClickListener(v -> {
            speak("بَ");
            addStar();
        });

        Button kasra = button("بِ   —   كسرة");
        kasra.setOnClickListener(v -> {
            speak("بِ");
            addStar();
        });

        Button damma = button("بُ   —   ضمة");
        damma.setOnClickListener(v -> {
            speak("بُ");
            addStar();
        });

        TextView note = new TextView(this);
        note.setText("جرّب لمس كل حركة واسمع صوتها 🔊");
        note.setTextSize(18);
        note.setGravity(Gravity.CENTER);
        root.addView(note);

        backButton();
    }

    private void showMadd() {
        prepareRoot();
        title("📏 المدود والمقاطع الأساسية");

        String[] syllables = {
                "با","بو","بي","ما","مو","مي",
                "سا","سو","سي","لا","لو","لي"
        };

        for (String s : syllables) {
            Button b = button(s + "   🔊");
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
            Button b = button(pictures[i] + "   " + words[i] + "   🔊");
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
                "هذا باب.","ماما تقرأ.","أنا أحب المدرسة.",
                "هذا قلم.","هذا كتاب.","أحب أبي وأمي."
        };

        for (String s : sentences) {
            Button b = button("🔊  " + s);
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
        q.setText("أين القلم؟ ✏️\n\nاختر الإجابة الصحيحة:");
        q.setTextSize(21);
        q.setGravity(Gravity.CENTER);
        q.setPadding(8, 10, 8, 20);
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
            for (int j = 0; j <= i; j++) objects.append("🍎");

            Button b = button(numbers[i] + "     " + objects + "   🔊");
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

        root.addView(input, new LinearLayout.LayoutParams(-1, 100));

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
        p.setText("الطالب: " + studentName +
                "\n\nالنجوم: ⭐ " + stars +
                "\n\nاستمر في التعلم والتدريب 🌟");
        p.setTextSize(24);
        p.setGravity(Gravity.CENTER);
        p.setPadding(10, 30, 10, 30);
        root.addView(p);

        Button reset = button("🔄 تصفير النجوم");
        reset.setOnClickListener(v -> {
            stars = 0;
            prefs.edit().putInt("stars", 0).apply();
            showProgress();
        });

        backButton();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
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
