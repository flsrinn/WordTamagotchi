import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class RankingPanel extends JPanel {
    private Ranking r = new Ranking();
    private Color purple = new Color(14, 13, 89);
    private StartPanel startPanel;

    // 각 레벨의 top 5 플레이어 출력
    public RankingPanel(StartPanel startPanel) {
	this.startPanel = startPanel;
	setLayout(null);

	JLabel ranking = new JLabel("Top 5");
	ranking.setFont(new Font("DungGeunMo", Font.PLAIN, 40));
	ranking.setLocation(390, 10);
	ranking.setSize(200, 40);
	add(ranking);

	ImageIcon crownIcon = new ImageIcon("image/crown.png");
	JLabel crown1 = new JLabel(new ImageIcon(crownIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
	crown1.setSize(40, 40);
	crown1.setLocation(350, 10);
	add(crown1);
	JLabel crown2 = new JLabel(new ImageIcon(crownIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
	crown2.setSize(40, 40);
	crown2.setLocation(490, 10);
	add(crown2);

	JLabel easy = new JLabel("EASY");
	easy.setFont(new Font("DungGeunMo", Font.PLAIN, 30));
	easy.setSize(100, 30);
	easy.setLocation(180, 50);
	add(easy);

	JLabel hard = new JLabel("HARD");
	hard.setFont(new Font("DungGeunMo", Font.PLAIN, 30));
	hard.setSize(100, 30);
	hard.setLocation(630, 50);
	add(hard);

	// top 5 정렬한 벡터 리턴 받기
	Vector<String> easyRank = r.getRank(1);
	Vector<String> hardRank = r.getRank(2);

	for (int i = 0; i < easyRank.size(); i++) {
	    JLabel num = new JLabel(Integer.toString(i + 1));
	    num.setFont(new Font("DungGeunMo", Font.BOLD, 50));
	    num.setForeground(purple);
	    num.setSize(30, 50);
	    num.setLocation(80, 90 + (80 * i));
	    add(num);

	    // 문자열에서 이름과 점수 분리 후 출력
	    String info = easyRank.get(i);
	    StringTokenizer st = new StringTokenizer(info, ",");
	    JLabel name = new JLabel(st.nextToken());
	    name.setSize(100, 30);
	    name.setFont(new Font("DungGeunMo", Font.PLAIN, 30));
	    name.setLocation(130, 100 + (80 * i));
	    add(name);

	    JLabel score = new JLabel(st.nextToken());
	    score.setSize(100, 30);
	    score.setFont(new Font("DungGeunMo", Font.PLAIN, 30));
	    score.setLocation(250, 100 + (80 * i));
	    add(score);
	}

	for (int i = 0; i < hardRank.size(); i++) {
	    JLabel num = new JLabel(Integer.toString(i + 1));
	    num.setFont(new Font("DungGeunMo", Font.BOLD, 50));
	    num.setForeground(purple);
	    num.setSize(30, 50);
	    num.setLocation(530, 90 + (80 * i));
	    add(num);

	    // 문자열에서 이름과 점수 분리 후 출력
	    String info = hardRank.get(i);
	    StringTokenizer st = new StringTokenizer(info, ",");
	    JLabel name = new JLabel(st.nextToken());
	    name.setSize(100, 30);
	    name.setFont(new Font("DungGeunMo", Font.PLAIN, 30));
	    name.setLocation(580, 100 + (80 * i));
	    add(name);

	    JLabel score = new JLabel(st.nextToken());
	    score.setSize(100, 30);
	    score.setFont(new Font("DungGeunMo", Font.PLAIN, 30));
	    score.setLocation(710, 100 + (80 * i));
	    add(score);
	}

	// 누르면 다시 시작 화면으로 돌아가는 버튼
	JButton returnButton = new JButton("RETURN");
	returnButton.setLocation(340, 460);
	returnButton.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
	returnButton.setOpaque(false);
	returnButton.setBackground(new Color(0, 0, 0, 0));
	returnButton.setSize(200, 40);
	returnButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		setVisible(false);
		startPanel.setVisible(true);
	    }
	});
	add(returnButton);

    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	ImageIcon backgroundIcon = new ImageIcon("image/background/startBackground.jpg");
	Image backgroundImg = backgroundIcon.getImage();
	g.drawImage(backgroundImg, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}