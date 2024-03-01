import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.sound.sampled.*;
import javax.swing.*;

public class StartPanel extends JPanel {
    private JLabel title = new JLabel("다마고치 키우기");
    private JButton startButton = new JButton("게임 시작");
    private JButton exitButton = new JButton("나가기");
    private JButton rankingButton = new JButton("랭킹 보기");
    private JButton addButton = new JButton("단어 추가");
    private JTextField player = new JTextField();
    private String playerName = "";
    private ImageIcon icon = new ImageIcon("image/background/startBackground.jpg");
    private Image backgroundImg = icon.getImage();
    private TextSource textSource = new TextSource(this);
    private int selectedLevel = -1;
    private RankingPanel rp = new RankingPanel(this);
    private GameFrame gameFrame;
    private Font descriptionFont = new Font("DungGeunMo", Font.PLAIN, 20);

    // 생성자
    public StartPanel(GameFrame gameFrame) {
	setLayout(null);
	this.gameFrame = gameFrame;
	setBackground(new Color(255, 171, 228));
	title.setFont(new Font("DungGeunMo", Font.PLAIN, 70));
	title.setLocation(180, 70);
	title.setSize(600, 100);
	add(title);
	buttons();
	getInfo();
    }

    @Override // 배경화면 그리기
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.drawImage(backgroundImg, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    // 사용자 이름과 게임 난이도를 입력 받는 메소드
    private void getInfo() {
	JLabel name = new JLabel("이름");
	name.setFont(descriptionFont);
	name.setLocation(350, 200);
	name.setSize(100, 20);
	add(name);

	// 이름을 입력받는 텍스트 필드
	player.setFont(descriptionFont);
	player.setLocation(400, name.getY() - 3);
	player.setSize(140, 30);
	add(player);

	JLabel level = new JLabel("레벨");
	level.setFont(descriptionFont);
	level.setLocation(350, 225);
	level.setSize(100, 50);
	add(level);

	// 게임 난이도 선택
	// easy는 1, hard는 2로 저장
	ButtonGroup levels = new ButtonGroup();
	JRadioButton easy = new JRadioButton("EASY");
	JRadioButton hard = new JRadioButton("HARD");
	easy.setOpaque(false);
	easy.setBackground(null);
	easy.setLocation(level.getX() + 55, level.getY() + 15);
	easy.setSize(70, 20);
	easy.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		selectedLevel = 1;
	    }
	});
	levels.add(easy);

	hard.setOpaque(false);
	hard.setBackground(null);
	hard.setLocation(easy.getX() + 75, level.getY() + 15);
	hard.setSize(70, 20);
	hard.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		selectedLevel = 2;
	    }
	});
	levels.add(hard);
	add(easy);
	add(hard);

    }

    // 키울 알을 선택하는 메소드
    public void chooseEgg() {
	removeAll();
	JLabel monsterDes = new JLabel("키울 알을 골라주세요!");
	monsterDes.setFont(new Font("DungGeunMo", Font.PLAIN, 40));
	monsterDes.setSize(500, 100);
	monsterDes.setLocation(230, 80);
	add(monsterDes);
	JButton buttons[] = new JButton[3];
	for (int i = 0; i < buttons.length; i++) {
	    String path = "image/monster/egg" + (i + 1) + ".png";
	    ImageIcon ic = new ImageIcon(path);
	    buttons[i] = new JButton(new ImageIcon(ic.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
	    buttons[i].setBounds(270 + i * 120, 200, 20, 15);
	    buttons[i].setSize(100, 100);
	    buttons[i].setOpaque(false);
	    buttons[i].setBackground(null);
	    add(buttons[i]);

	    int index = i;
	    buttons[i].addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    removeAll();
		    gameFrame.setGameInfo(selectedLevel, 1, index, playerName);
		    gameFrame.toGamePanel();
		}
	    });
	}
	revalidate();
	repaint();
    }

    private void buttons() {
	// 게임 시작 버튼
	startButton.setOpaque(false);
	startButton.setBackground(null);
	startButton.setFont(descriptionFont);
	startButton.setLocation(340, 280);
	startButton.setSize(200, 40);
	add(startButton);
	startButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		playerName = player.getText();
		// 이름, 레벨 미선택
		if (selectedLevel == -1 && playerName.equals("")) {
		    JOptionPane.showMessageDialog(null, "플레이어 이름을 입력해주세요.");
		    JOptionPane.showMessageDialog(null, "플레이할 레벨을 선택해주세요.");
		}
		// 레벨 미선택
		else if (selectedLevel == -1) {
		    JOptionPane.showMessageDialog(null, "플레이할 레벨을 선택해주세요.");
		}
		// 이름 미입력
		else if (playerName.equals("")) {
		    JOptionPane.showMessageDialog(null, "플레이어 이름을 입력해주세요.");
		}
		// 이름과 레벨 모두 선택했을 경우 알 선택으로 넘어감
		else {
		    chooseEgg();
		}
	    }

	});

	// 단어 추가 버튼
	addButton.setLocation(340, 340);
	addButton.setSize(200, 40);
	addButton.setOpaque(false);
	addButton.setBackground(null);
	addButton.setFont(descriptionFont);
	addButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String newWord = JOptionPane.showInputDialog("추가할 단어를 입력해주세요.");
		if (newWord == null)
		    return;
		newWord.trim();

		// 이미 텍스트 파일에 단어가 존재할 경우
		if (textSource.search(newWord)) {
		    JOptionPane.showMessageDialog(null, "이미 존재하는 단어입니다.");
		}
		// 알파벳 외에 다른 글자를 작성했거나 아무 단어도 작성하지 않았을 때
		else if (newWord.equals("") || !newWord.matches("[a-zA-Z]+")) {
		    JOptionPane.showMessageDialog(null, "영어 단어를 입력해주세요.", "단어 추가 오류", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		// 올바르게 단어를 입력했을 경우 텍스트 파일에 추가
		else {
		    textSource.add(newWord);
		}
	    }
	});
	add(addButton);

	// Top 5를 확인하는 버튼
	rankingButton.setOpaque(false);
	rankingButton.setBackground(null);
	rankingButton.setFont(descriptionFont);
	rankingButton.setLocation(340, 400);
	rankingButton.setSize(200, 40);
	rankingButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		getParent().add(rp);
		rp.setVisible(true);
		setVisible(false);
	    }
	});
	add(rankingButton);

	// 나가기 버튼
	exitButton.setOpaque(false);
	exitButton.setBackground(null);
	exitButton.setFont(descriptionFont);
	exitButton.setLocation(340, 460);
	exitButton.setSize(200, 40);
	add(exitButton);
	exitButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		System.exit(0);
	    }
	});
    }

}
