import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class ScorePanel extends JPanel {
    private Ranking rank = new Ranking();
    private MonsterPanel monsterPanel;
    private GamePanel gamePanel;
    private int score;
    private JLabel life[];
    private JLabel scoreText = new JLabel("SCORE ");
    private JLabel scoreLabel, player, playerInfo, stageInfo;
    private int lifes, maxLife;
    private ImageIcon heart = new ImageIcon("image/heart.png");
    private ImageIcon emptyHeart = new ImageIcon("image/emptyHeart.png");
    private ImageIcon background = new ImageIcon("image/cloud.png");
    private String playerName;
    private int level, stage;
    private Color purple = new Color(14, 13, 89);
    private Clip clip;

    public ScorePanel(int level, int stage, String playerName, MonsterPanel monsterPanel, GamePanel gamePanel) {
	this.level = level;
	this.stage = stage;
	this.playerName = playerName;
	this.monsterPanel = monsterPanel;
	this.gamePanel = gamePanel;
	setLayout(null);
	setBackground(purple);

	// 각 스테이지에 맞게 생명, 점수 초기화
	if (stage == 1) {
	    lifes = 3;
	    maxLife = 3;
	    score = 0;
	} else if (stage == 2) {
	    lifes = 4;
	    maxLife = 4;
	    score = 100;
	} else if (stage == 3) {
	    lifes = 5;
	    maxLife = 5;
	    score = 250;
	} else {
	    lifes = 5;
	    maxLife = 5;
	    score = 400;
	}
	life = new JLabel[lifes];

	// 점수
	scoreLabel = new JLabel(Integer.toString(score));
	scoreLabel.setFont(new Font("DungGeunMo", Font.BOLD, 20));
	scoreLabel.setLocation(135, 20);
	scoreLabel.setSize(100, 20);
	scoreLabel.setForeground(Color.white);
	add(scoreLabel);

	scoreText.setFont(new Font("DungGeunMo", Font.BOLD, 20));
	scoreText.setLocation(70, 20);
	scoreText.setSize(100, 20);
	scoreText.setForeground(Color.white);
	add(scoreText);

	// 현재 스테이지 정보 출력
	if (stage > 3) {
	    stageInfo = new JLabel("EXTRA STAGE");
	    stageInfo.setFont(new Font("DungGeunMo", Font.BOLD, 30));
	    stageInfo.setSize(250, 20);
	    stageInfo.setLocation(30, 100);
	} else {
	    stageInfo = new JLabel("STAGE " + Integer.toString(stage));
	    stageInfo.setFont(new Font("DungGeunMo", Font.BOLD, 30));
	    stageInfo.setSize(150, 30);
	    stageInfo.setLocation(60, 100);
	}
	stageInfo.setForeground(Color.white);
	add(stageInfo);

	// 현재 플레이어 이름 출력
	playerInfo = new JLabel("현재 플레이어");
	playerInfo.setFont(new Font("DungGeunMo", Font.BOLD, 20));
	playerInfo.setSize(200, 20);
	playerInfo.setForeground(Color.white);
	playerInfo.setLocation(45, 160);
	add(playerInfo);

	player = new JLabel(playerName);
	player.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
	player.setSize(140, 20);
	player.setForeground(Color.white);
	player.setLocation(45, 200);
	add(player);

	// 생명 이미지 설정
	for (int i = 0; i < life.length; i++) {
	    life[i] = new JLabel(new ImageIcon(heart.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
	}

	// 게임 일시정지 버튼
	Image pauseImage = new ImageIcon("image/pause.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	Image pausePressed = new ImageIcon("image/pausePressed.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	JButton pauseButton = new JButton(new ImageIcon(pauseImage));
	pauseButton.setPressedIcon(new ImageIcon(pausePressed));
	pauseButton.setContentAreaFilled(false);
	pauseButton.setSize(40, 40);
	pauseButton.setLocation(60, 240);
	pauseButton.setOpaque(false);
	pauseButton.setBackground(null);
	pauseButton.setForeground(Color.white);
	pauseButton.setBorderPainted(false);
	pauseButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		gamePanel.gameGround.gameth.stopRain(true);
		gamePanel.gameGround.wordth.stopWord(true);
	    }
	});
	add(pauseButton);

	// 게임 재시작 버튼
	Image playImage = new ImageIcon("image/play.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	Image playPressed = new ImageIcon("image/playPressed.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	JButton playButton = new JButton(new ImageIcon(playImage));
	playButton.setPressedIcon(new ImageIcon(playPressed));
	playButton.setContentAreaFilled(false);
	playButton.setSize(40, 40);
	playButton.setLocation(130, 240);
	playButton.setOpaque(false);
	playButton.setBackground(null);
	playButton.setForeground(Color.white);
	playButton.setBorderPainted(false);
	playButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		gamePanel.gameGround.gameth.resumeRain();
		gamePanel.gameGround.wordth.resumeWord();
	    }
	});
	add(playButton);
    }

    @Override // 생명 출력
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	for (int i = 0; i < life.length; i++) {
	    if (stage == 1) {
		life[i].setLocation(70 + i * 30, 50);
	    } else if (stage == 2) {
		life[i].setLocation(55 + i * 30, 50);
	    } else {
		life[i].setLocation(40 + i * 30, 50);
	    }
	    life[i].setSize(30, 30);
	    add(life[i]);
	}
    }

    // 점수를 증가시키는 메소드
    public boolean increase(int score, int stage) {
	this.score += score;
	scoreLabel.setText(Integer.toString(this.score));
	loadAudio("audio/increase.wav");
	// 몬스터의 이미지를 바꿈
	if (score == 0) {
	    monsterPanel.changeImg(0);
	} else {
	    monsterPanel.changeImg(1);
	}
	// 각 스테이지마다 목표 점수에 도달할 경우 true 리턴
	if (this.score >= 20 && stage == 1) {
	    monsterPanel.mt.interrupt();
	    return true;
	} else if (this.score >= 120 && stage == 2) {
	    monsterPanel.mt.interrupt();
	    return true;
	} else if (this.score >= 270 && stage == 3) {
	    monsterPanel.mt.interrupt();
	    return true;
	}
	// 목표 점수에 도달하지 못했을 경우 false 리턴
	return false;
    }

    // 생명을 감소시키는 메소드
    public boolean decrease(int stage) {
	// 생명을 감소시키고 남은 생명을 다시 그림
	// 생명이 감소하면 빈 하트 출력
	for (int i = 0; i < lifes; i++) {
	    remove(life[i]);
	}
	lifes--;
	life[lifes] = new JLabel(new ImageIcon(emptyHeart.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
	repaint();
	monsterPanel.changeImg(2);
	loadAudio("audio/decrease.wav");
	// 생명이 0이 될 경우 true 리턴
	if (lifes == 0) {
	    gamePanel.gameGround.gameth.interrupt();
	    gamePanel.gameGround.wordth.interrupt();
	    int choose = JOptionPane.showConfirmDialog(null, "랭킹에 등록하시겠습니까?", "게임 오버", JOptionPane.YES_NO_OPTION);
	    if (choose == JOptionPane.YES_OPTION)
		rank.addRanking(level, playerName, score);
	    gamePanel.gameOver(score);
	    loadAudio("audio/gameOver.wav");
	    return true;
	}
	// 생명이 1 이상일 경우 false 리턴
	return false;
    }

    // 오디오 재생
    private void loadAudio(String fileName) {
	try {
	    clip = AudioSystem.getClip();
	    File audioFile = new File(fileName);
	    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
	    clip.open(audioStream);
	    clip.start();
	} catch (LineUnavailableException e) {
	    e.printStackTrace();
	} catch (UnsupportedAudioFileException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    // 생명 1 추가
    public void addLife() {
	// 이미 생명이 가득 차있을 경우 리턴
	if (lifes == maxLife) {
	    return;
	}
	// 생명을 증가시키고 남은 생명을 다시 그림
	life[lifes] = new JLabel(new ImageIcon(heart.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
	lifes++;
	repaint();
    }

}
