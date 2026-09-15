package com.yemeni.huroofna;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * حروفنا - الواجهة التعليمية الأساسية
 * نسخة أولى متوافقة مع Android SDK فقط، بدون مكتبات خارجية.
 * الهوية: عربية RTL، عمودية، ألوان طفولية، ملفات طلاب، نجوم، صوت.
 */
public class MainActivity extends Activity {

    private LinearLayout root;
    private TextToSpeech tts;
    private final ArrayList<Student> students = new ArrayList<>();
    private int activeStudent = 0;

    private final String[] letters = {
            "ا","ب","ت","ث","ج","ح","خ","د","ذ","ر","ز","س","ش","ص",
            "ض","ط","ظ","ع","غ","ف","ق","ك","ل","م","ن","هـ","و","ي"
    };

    private final String[] letterNames = {
            "ألف","باء","تاء","ثاء","جيم","حاء","خاء","دال","ذال","راء","زاي","سين",
            "شين","صاد","ضاد","طاء","ظاء","عين","غين","فاء","قاف","كاف","لام","ميم",
            "نون","هاء","واو","ياء"
    };

    private final String[] words = {
            "أسد","بطة","تفاحة","ثعلب","جمل","حصان","خروف","دجاجة","ذئب","ريشة",
            "زهرة","سمكة","شجرة","صقر","ضفدع","طائر","ظرف","عين","غيمة","فراشة",
            "قمر","كتاب","ليمون","موز","نحلة","هدية","وردة","يد"
    };

    private final String[] pictures = {
            "🦁","🦆","🍎","🦊","🐪","🐎","🐑","🐔","🐺","🪶",
            "🌷","🐟","🌳","🦅","🐸","🐦","✉️","👁️","☁️","🦋",
            "🌙","📘","🍋","🍌","🐝","🎁","🌹","✋"
    };

    private static class Student {
        String name;
        int stars;
        Set<Integer> learned = new HashSet<>();
        int lastLetter = 0;
        Student(String name) { this.name = name; }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        students.add(new Student("أحمد"));
        students.get(0).stars = 12;
        students.add(new Student("سارة"));
        students.get(1).stars = 20;
        students.add(new Student("محمد"));
        students.get(2).stars = 7;

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int r = tts.setLanguage(new Locale("ar"));
                tts.setSpeechRate(0.72f);
            }
        });

        showHome();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private int blue = Color.rgb(30, 136, 229);
    private int darkBlue = Color.rgb(25, 76, 130);
    private int green = Color.rgb(43, 180, 80);
    private int yellow = Color.rgb(255, 193, 7);
    private int pink = Color.rgb(233, 74, 145);
    private int purple = Color.rgb(123, 75, 190);
    private int orange = Color.rgb(245, 135, 25);

    private void base(String title) {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(16, 18, 16, 10);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundColor(Color.rgb(239, 249, 255));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.addView(root);

        setContentView(scroll);

        TextView bar = text(title, 24, Color.WHITE, true);
        bar.setGravity(Gravity.CENTER);
        bar.setPadding(10, 16, 10, 16);
        bar.setBackground(round(blue, 28));
        root.addView(bar, lp(-1, -2, 8));
    }

    private TextView text(String s, float size, int color, boolean bold) {
        TextView v = new TextView(this);
        v.setText(s);
        v.setTextSize(size);
        v.setTextColor(color);
        v.setGravity(Gravity.CENTER);
        v.setTypeface(Typeface.DEFAULT, bold ? Typeface.BOLD : Typeface.NORMAL);
        v.setPadding(8, 8, 8, 8);
        return v;
    }

    private GradientDrawable round(int color, float radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    private Button button(String label, int color) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(17);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setBackground(round(color, 35));
        b.setPadding(14, 8, 14, 8);
        return b;
    }

    private LinearLayout card() {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setGravity(Gravity.CENTER);
        c.setPadding(12, 12, 12, 12);
        c.setBackground(round(Color.WHITE, 28));
        return c;
    }

    private LinearLayout row() {
        LinearLayout r = new LinearLayout(this);
        r.setOrientation(LinearLayout.HORIZONTAL);
        r.setGravity(Gravity.CENTER);
        return r;
    }

    private LinearLayout.LayoutParams lp(int w, int h, int bottom) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(w, h);
        p.setMargins(6, 6, 6, bottom);
        return p;
    }

    private void speak(String s) {
        if (tts != null) {
            tts.speak(s, TextToSpeech.QUEUE_FLUSH, null, "huroofna_" + System.currentTimeMillis());
        }
    }

    private Student current() {
        if (students.isEmpty()) {
            students.add(new Student("طالب"));
        }
        if (activeStudent >= students.size()) activeStudent = 0;
        return students.get(activeStudent);
    }

    private void showHome() {
        base("⭐ حروفنا ⭐");

        TextView logo = text("حُرُوفنا\nمعًا نقرأ .. ونبدع", 31, darkBlue, true);
        logo.setBackground(round(Color.rgb(220, 245, 255), 32));
        root.addView(logo, lp(-1, -2, 8));

        TextView welcome = text("مرحبًا يا بطل! 👦👧\n" +
                "الطالب الحالي: " + current().name +
                "    ⭐ " + current().stars, 20, darkBlue, true);
        root.addView(welcome, lp(-1, -2, 6));

        Button start = button("🚀 ابدأ التعلم", green);
        start.setOnClickListener(v -> showLetters());
        root.addView(start, lp(-1, -2, 8));

        LinearLayout r1 = row();
        Button studentsBtn = button("👦👧 الطلاب", blue);
        studentsBtn.setOnClickListener(v -> showStudents());
        Button progressBtn = button("📊 التقدم", purple);
        progressBtn.setOnClickListener(v -> showProgress());
        r1.addView(studentsBtn, lp(0, -2, 0)); 
        ((LinearLayout.LayoutParams)studentsBtn.getLayoutParams()).weight = 1;
        r1.addView(progressBtn, lp(0, -2, 0));
        ((LinearLayout.LayoutParams)progressBtn.getLayoutParams()).weight = 1;
        root.addView(r1, lp(-1, -2, 5));

        LinearLayout r2 = row();
        Button games = button("🎮 الألعاب", pink);
        games.setOnClickListener(v -> showGames());
        Button write = button("✍️ الكتابة", orange);
        write.setOnClickListener(v -> showWriting());
        r2.addView(games, lp(0, -2, 0));
        ((LinearLayout.LayoutParams)games.getLayoutParams()).weight = 1;
        r2.addView(write, lp(0, -2, 0));
        ((LinearLayout.LayoutParams)write.getLayoutParams()).weight = 1;
        root.addView(r2, lp(-1, -2, 8));

        TextView slogan = text("🔤 الحرف → الحركة → السكون → المد → المقطع → الكلمة → الجملة → الفهم", 16, darkBlue, true);
        slogan.setBackground(round(Color.rgb(255, 250, 220), 24));
        root.addView(slogan, lp(-1, -2, 8));

        addBottomNav();
    }

    private void addBottomNav() {
        LinearLayout nav = row();
        Button home = button("الرئيسية", blue);
        home.setOnClickListener(v -> showHome());
        Button lettersBtn = button("الحروف", green);
        lettersBtn.setOnClickListener(v -> showLetters());
        Button games = button("الألعاب", pink);
        games.setOnClickListener(v -> showGames());
        Button prog = button("التقدم", purple);
        prog.setOnClickListener(v -> showProgress());

        Button[] bs = {home, lettersBtn, games, prog};
        for (Button b : bs) {
            nav.addView(b, lp(0, -2, 0));
            ((LinearLayout.LayoutParams)b.getLayoutParams()).weight = 1;
        }
        root.addView(nav, lp(-1, -2, 0));
    }

    private void showStudents() {
        base("👦👧 اختيار الطالب");

        root.addView(text("اختر الطالب الذي تريد متابعة تعلمه", 19, darkBlue, true), lp(-1, -2, 8));

        for (int i = 0; i < students.size(); i++) {
            final int index = i;
            Student s = students.get(i);
            LinearLayout c = card();

            LinearLayout r = row();
            TextView avatar = text(i % 2 == 0 ? "👦" : "👧", 38, darkBlue, false);
            TextView info = text(s.name + "\nالصف الأول\n⭐ " + s.stars + " نجمة  •  " +
                    s.learned.size() + "/28 حرفًا", 18, darkBlue, true);
            r.addView(avatar, lp(0, -2, 0));
            ((LinearLayout.LayoutParams)avatar.getLayoutParams()).weight = 1;
            r.addView(info, lp(0, -2, 0));
            ((LinearLayout.LayoutParams)info.getLayoutParams()).weight = 3;
            c.addView(r);

            Button choose = button(index == activeStudent ? "✓ الطالب الحالي" : "اختيار الطالب", index == activeStudent ? green : blue);
            choose.setOnClickListener(v -> {
                activeStudent = index;
                showHome();
            });
            c.addView(choose, lp(-1, -2, 0));
            root.addView(c, lp(-1, -2, 8));
        }

        Button add = button("➕ إضافة طالب جديد", green);
        add.setOnClickListener(v -> addStudentDialog());
        root.addView(add, lp(-1, -2, 8));

        Button back = button("↩ العودة للرئيسية", darkBlue);
        back.setOnClickListener(v -> showHome());
        root.addView(back, lp(-1, -2, 0));
    }

    private void addStudentDialog() {
        final EditText input = new EditText(this);
        input.setHint("اكتب اسم الطالب");
        input.setTextSize(18);
        input.setGravity(Gravity.CENTER);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("إضافة طالب")
                .setMessage("سيكون للطالب ملف وتقدم ونجوم مستقلة.")
                .setView(input)
                .setNegativeButton("إلغاء", null)
                .setPositiveButton("إضافة", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = input.getText().toString().trim();
            if (name.length() < 1) {
                input.setError("اكتب اسم الطالب");
                return;
            }
            students.add(new Student(name));
            activeStudent = students.size() - 1;
            dialog.dismiss();
            showStudents();
        }));
        dialog.show();
    }

    private void showLetters() {
        base("🔤 الحروف الهجائية");

        root.addView(text("اختر حرفًا للتعلم والاستماع والتدريب", 18, darkBlue, true), lp(-1, -2, 8));

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(4);
        grid.setUseDefaultMargins(true);

        for (int i = 0; i < letters.length; i++) {
            final int index = i;
            LinearLayout c = card();
            c.setPadding(6, 8, 6, 8);

            TextView l = text(letters[i], 34, darkBlue, true);
            TextView w = text(pictures[i] + "\n" + words[i], 15, darkBlue, true);
            c.addView(l);
            c.addView(w);

            c.setOnClickListener(v -> showLetter(index));
            grid.addView(c, new ViewGroup.LayoutParams(0, -2));
            GridLayout.LayoutParams gp = (GridLayout.LayoutParams)c.getLayoutParams();
            gp.width = 0;
            gp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            gp.setMargins(5, 5, 5, 5);
            c.setLayoutParams(gp);
        }

        root.addView(grid, lp(-1, -2, 8));

        Button back = button("↩ الرئيسية", darkBlue);
        back.setOnClickListener(v -> showHome());
        root.addView(back, lp(-1, -2, 0));
    }

    private void showLetter(int index) {
        current().lastLetter = index;
        base("🔤 الحرف " + letters[index]);

        TextView big = text(letters[index] + "\n" + pictures[index], 72, darkBlue, true);
        big.setBackground(round(Color.WHITE, 35));
        root.addView(big, lp(-1, -2, 6));

        TextView name = text("اسم الحرف: " + letterNames[index] + "\nكلمة: " + words[index], 21, darkBlue, true);
        root.addView(name, lp(-1, -2, 6));

        Button sound = button("🔊 اسمع الحرف", blue);
        sound.setOnClickListener(v -> speak(letterNames[index] + ". " + letters[index] + ". " + words[index]));
        root.addView(sound, lp(-1, -2, 8));

        LinearLayout vowelRow = row();
        String[] marks = {"َ","ِ","ُ"};
        for (String mark : marks) {
            final String spoken = letterNames[index] + mark;
            Button b = button(letters[index] + mark, purple);
            b.setOnClickListener(v -> speak(spoken));
            vowelRow.addView(b, lp(0, -2, 0));
            ((LinearLayout.LayoutParams)b.getLayoutParams()).weight = 1;
        }
        root.addView(vowelRow, lp(-1, -2, 6));

        Button sukoon = button(letters[index] + "ْ   ⭕ السكون", orange);
        sukoon.setOnClickListener(v -> speak(letterNames[index] + " ساكن"));
        root.addView(sukoon, lp(-1, -2, 6));

        Button mad = button("📏 المدود: " + letters[index] + "ا   " + letters[index] + "و   " + letters[index] + "ي", green);
        mad.setOnClickListener(v -> speak(letters[index] + "ا، " + letters[index] + "و، " + letters[index] + "ي"));
        root.addView(mad, lp(-1, -2, 6));

        Button positions = button("📍 أول الكلمة | وسط الكلمة | آخر الكلمة", blue);
        positions.setOnClickListener(v -> showPositions(index));
        root.addView(positions, lp(-1, -2, 6));

        Button word = button("🧩 المقطع والكلمة والصورة", pink);
        word.setOnClickListener(v -> showWord(index));
        root.addView(word, lp(-1, -2, 6));

        Button sentence = button("📖 جمل بسيطة", purple);
        sentence.setOnClickListener(v -> showSentences(index));
        root.addView(sentence, lp(-1, -2, 6));

        Button learn = button("⭐ أنجزت هذا الحرف", green);
        learn.setOnClickListener(v -> {
            if (!current().learned.contains(index)) {
                current().learned.add(index);
                current().stars += 1;
            }
            Toast.makeText(this, "أحسنت! ⭐ تم حفظ تقدمك", Toast.LENGTH_SHORT).show();
            showLetter(index);
        });
        root.addView(learn, lp(-1, -2, 8));

        Button back = button("↩ العودة للحروف", darkBlue);
        back.setOnClickListener(v -> showLetters());
        root.addView(back, lp(-1, -2, 0));
    }

    private void showPositions(int i) {
        base("📍 موقع الحرف في الكلمة");

        String l = letters[i];
        String[] examples = {
                "في أول الكلمة",
                "في وسط الكلمة",
                "في آخر الكلمة"
        };

        for (int k = 0; k < 3; k++) {
            LinearLayout c = card();
            c.addView(text(examples[k], 18, darkBlue, true));
            String shown;
            if (i == 0) shown = "أَسَد";
            else if (i == 1) shown = "بَاب";
            else if (i == 2) shown = "كِتَاب";
            else if (i == 3) shown = "ثَعْلَب";
            else shown = words[i] + "\nالحرف: " + l;

            c.addView(text(pictures[i] + "\n" + shown, 23, darkBlue, true));
            Button b = button("🔊 استمع", blue);
            b.setOnClickListener(v -> speak(shown));
            c.addView(b);
            root.addView(c, lp(-1, -2, 7));
        }

        Button back = button("↩ العودة للحرف", darkBlue);
        back.setOnClickListener(v -> showLetter(i));
        root.addView(back, lp(-1, -2, 0));
    }

    private void showWord(int i) {
        base("🧩 المقاطع والكلمة");

        String l = letters[i];
        String[] syllables = {"بَ","بِ","بُ"};
        if (i != 1) {
            syllables = new String[]{l + "َ", l + "ِ", l + "ُ"};
        }

        root.addView(text("المقطع", 21, darkBlue, true), lp(-1, -2, 4));
        LinearLayout r = row();
        for (String s : syllables) {
            Button b = button(s + " 🔊", purple);
            b.setOnClickListener(v -> speak(s));
            r.addView(b, lp(0, -2, 0));
            ((LinearLayout.LayoutParams)b.getLayoutParams()).weight = 1;
        }
        root.addView(r, lp(-1, -2, 10));

        LinearLayout c = card();
        c.addView(text(pictures[i], 60, darkBlue, false));
        c.addView(text(words[i], 28, darkBlue, true));
        Button sound = button("🔊 استمع للكلمة", blue);
        sound.setOnClickListener(v -> speak(words[i]));
        c.addView(sound);
        root.addView(c, lp(-1, -2, 10));

        Button back = button("↩ العودة للحرف", darkBlue);
        back.setOnClickListener(v -> showLetter(i));
        root.addView(back, lp(-1, -2, 0));
    }

    private void showSentences(int i) {
        base("📖 الجمل");

        String[] sentences = {
                "هذا " + words[i] + ".",
                "أنا أحب القراءة.",
                "ماما تقرأ.",
                "هذا كتاب."
        };

        for (String s : sentences) {
            LinearLayout c = card();
            c.addView(text(s, 22, darkBlue, true));
            Button b = button("🔊 استمع", blue);
            b.setOnClickListener(v -> speak(s));
            c.addView(b);
            root.addView(c, lp(-1, -2, 7));
        }

        Button comprehension = button("🧠 تدريبات الفهم", green);
        comprehension.setOnClickListener(v -> showComprehension());
        root.addView(comprehension, lp(-1, -2, 7));

        Button back = button("↩ العودة للحرف", darkBlue);
        back.setOnClickListener(v -> showLetter(i));
        root.addView(back, lp(-1, -2, 0));
    }

    private void showComprehension() {
        base("🧠 تدريبات الفهم");

        root.addView(text("اقرأ الجملة ثم اختر الإجابة الصحيحة", 19, darkBlue, true), lp(-1, -2, 10));

        LinearLayout c = card();
        String sentence = "هذا باب. ماما تفتح الباب.";
        c.addView(text("📖 " + sentence, 22, darkBlue, true));

        c.addView(text("ماذا تفتح ماما؟", 20, darkBlue, true), lp(-1, -2, 6));

        String[] answers = {"📘 الكتاب", "🚪 الباب", "🍎 التفاحة"};
        for (String a : answers) {
            Button b = button(a, a.contains("الباب") ? green : blue);
            b.setOnClickListener(v -> {
                if (a.contains("الباب")) {
                    current().stars++;
                    Toast.makeText(this, "إجابة صحيحة ⭐", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "حاول مرة أخرى", Toast.LENGTH_SHORT).show();
                }
            });
            c.addView(b, lp(-1, -2, 5));
        }
        root.addView(c, lp(-1, -2, 10));

        Button back = button("↩ الرئيسية", darkBlue);
        back.setOnClickListener(v -> showHome());
        root.addView(back, lp(-1, -2, 0));
    }

    private void showGames() {
        base("🎮 الألعاب");

        root.addView(text("اختر لعبة تعليمية", 21, darkBlue, true), lp(-1, -2, 8));

        String[] games = {
                "🔎 أين الحرف؟",
                "🖼️ اختر الصورة المناسبة",
                "🔊 اسمع واختر",
                "🧩 أكمل الكلمة",
                "📍 حدد موقع الحرف",
                "✓✗ صح أم خطأ"
        };

        for (int i = 0; i < games.length; i++) {
            final int game = i;
            Button b = button(games[i], new int[]{blue,pink,purple,green,orange,blue}[i]);
            b.setOnClickListener(v -> playGame(game));
            root.addView(b, lp(-1, -2, 7));
        }

        Button back = button("↩ الرئيسية", darkBlue);
        back.setOnClickListener(v -> showHome());
        root.addView(back, lp(-1, -2, 0));
    }

    private void playGame(int game) {
        base("🎮 لعبة تعليمية");

        final int target = (current().lastLetter + game) % letters.length;
        String prompt;
        if (game == 1) prompt = "أي صورة تبدأ بحرف " + letters[target] + "؟";
        else if (game == 2) prompt = "استمع ثم اختر الحرف";
        else prompt = "اختر الحرف الصحيح: " + letters[target];

        root.addView(text(prompt, 22, darkBlue, true), lp(-1, -2, 10));

        if (game == 1) {
            String[] imgs = {pictures[target], "🍎", "📘", "🐟"};
            for (int i=0;i<imgs.length;i++) {
                final int x=i;
                Button b = button(imgs[i], x==0?green:blue);
                b.setTextSize(38);
                b.setOnClickListener(v -> {
                    if (x==0) {
                        current().stars++;
                        Toast.makeText(this, "أحسنت ⭐", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(this, "حاول مرة أخرى", Toast.LENGTH_SHORT).show();
                });
                root.addView(b, lp(-1, -2, 6));
            }
        } else {
            String[] opts = {letters[target], "ب", "م", "س"};
            for (int i=0;i<opts.length;i++) {
                final boolean correct = i==0;
                Button b = button(opts[i], correct?green:blue);
                b.setTextSize(30);
                b.setOnClickListener(v -> {
                    if (correct) {
                        current().stars++;
                        current().learned.add(target);
                        Toast.makeText(this, "إجابة صحيحة ⭐", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(this, "حاول مرة أخرى", Toast.LENGTH_SHORT).show();
                });
                root.addView(b, lp(-1, -2, 6));
            }
            Button listen = button("🔊 استمع", purple);
            listen.setOnClickListener(v -> speak(letterNames[target]));
            root.addView(listen, lp(-1, -2, 8));
        }

        Button back = button("↩ الألعاب", darkBlue);
        back.setOnClickListener(v -> showGames());
        root.addView(back, lp(-1, -2, 0));
    }

    private void showWriting() {
        base("✍️ تدريب الكتابة والتتبع");

        int i = current().lastLetter;
        TextView guide = text("اتبع الحرف بإصبعك", 22, darkBlue, true);
        root.addView(guide, lp(-1, -2, 8));

        TextView trace = text(letters[i], 110, Color.rgb(90, 160, 220), true);
        trace.setBackground(round(Color.WHITE, 35));
        root.addView(trace, lp(-1, 220, 10));

        Button listen = button("🔊 اسمع الحرف", blue);
        listen.setOnClickListener(v -> speak(letterNames[i]));
        root.addView(listen, lp(-1, -2, 6));

        Button done = button("⭐ أنجزت التدريب", green);
        done.setOnClickListener(v -> {
            current().stars++;
            Toast.makeText(this, "أحسنت! ⭐", Toast.LENGTH_SHORT).show();
        });
        root.addView(done, lp(-1, -2, 8));

        Button back = button("↩ الرئيسية", darkBlue);
        back.setOnClickListener(v -> showHome());
        root.addView(back, lp(-1, -2, 0));
    }

    private void showProgress() {
        base("🏆 تقدم الطالب");

        Student s = current();
        int learned = s.learned.size();

        TextView info = text("👦👧 " + s.name + "\n⭐ النجوم: " + s.stars +
                "\n🔤 الحروف المتقنة: " + learned + " / 28", 23, darkBlue, true);
        info.setBackground(round(Color.WHITE, 30));
        root.addView(info, lp(-1, -2, 10));

        ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        pb.setMax(28);
        pb.setProgress(learned);
        root.addView(pb, lp(-1, 35, 10));

        String[] units = {
                "🔤 الحروف الهجائية",
                "َ ِ ُ الحركات",
                "⭕ السكون",
                "📏 المدود",
                "🧩 المقاطع",
                "📍 موقع الحرف",
                "🖼️ الكلمات والصور",
                "📖 الجمل",
                "🧠 الفهم",
                "🎮 الألعاب",
                "✍️ الكتابة"
        };

        for (String u : units) {
            TextView t = text("✓  " + u, 18, darkBlue, true);
            t.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            t.setBackground(round(Color.WHITE, 22));
            root.addView(t, lp(-1, -2, 5));
        }

        Button back = button("↩ الرئيسية", darkBlue);
        back.setOnClickListener(v -> showHome());
        root.addView(back, lp(-1, -2, 0));
    }
}
