import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class GameFrame extends JFrame {
    private StartPanel startPanel;
    private GamePanel gamePanel;
    private int level, monsterIndex, stage;
    private String playerName;
    private Clip clip;
    private NextStage nextStage = new NextStage(this, stage);

    public GameFrame() {
	setTitle("다마고치 게임");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	Container c = getContentPane();
	c.setBackground(new Color(56, 25, 160));
	toStartPanel();
	loadAudio();
	menu();
	setSize(900, 600);
	setResizable(false);
	setVisible(true);
    }

    // 게임 정보를 설정하는 메소드
    public void setGameInfo(int level, int stage, int monsterIndex, String playerName) {
	this.level = level;
	this.stage = stage;
	this.monsterIndex = monsterIndex;
	this.playerName = playerName;
    }

    // 게임 정보(레벨, 스테이지, 몬스터, 플레이어 이름)를 리턴하는 메소드
    public int getLevel() {
	return level;
    }

    public int getStage() {
	return stage;
    }

    public int getMonster() {
	return monsterIndex;
    }

    public String getPlayerName() {
	return playerName;
    }

    // NextStage 패널을 보이게 하는 메소드
    public void toNextStage() {
	nextStage = new NextStage(this, stage);
	getContentPane().add(nextStage);
	remove(gamePanel);
	revalidate();
    }

    // GamePanel을 보이게 하는 메소드
    public void toGamePanel() {
	// startPanel, nextStage 패널이 존재할 경우 지움
	if (startPanel != null) {
	    remove(startPanel);
	}
	if (nextStage != null) {
	    remove(nextStage);
	}
	gamePanel = new GamePanel(this, playerName);
	getContentPane().add(gamePanel);
	revalidate();
    }

    // StartPanel을 보이게 하는 메소드
    public void toStartPanel() {
	// gamePanel, nextStage 패널이 존재할 경우 지움
	if (nextStage != null) {
	    remove(nextStage);
	}
	if (gamePanel != null) {
	    remove(gamePanel);
	}
	startPanel = new StartPanel(this);
	getContentPane().add(startPanel);
	revalidate();
	repaint();
    }

    private void menu() {
	JToolBar bar = new JToolBar("game");

	// 배경 음악을 재생하는 버튼
	JButton music = new JButton("music");
	music.setFont(new Font("DungGeunMo", Font.PLAIN, 18));
	music.setToolTipText("배경 음악을 틉니다.");
	music.setOpaque(false);
	music.setForeground(Color.white);
	music.setFocusPainted(false);
	music.setFocusable(false);
	music.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		clip.start();
	    }
	});
	bar.add(music);

	// 배경 음악 소리를 키우는 버튼
	JButton soundUp = new JButton("sound+");
	soundUp.setFont(new Font("DungGeunMo", Font.PLAIN, 18));
	soundUp.setToolTipText("소리를 높입니다.");
	soundUp.setOpaque(false);
	soundUp.setForeground(Color.white);
	soundUp.setFocusPainted(false);
	soundUp.setFocusable(false);
	soundUp.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
		    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		    float range = gainControl.getMaximum() - gainControl.getMinimum();
		    float gain = (range * 10 / 100) + gainControl.getValue(); // Increase volume by 1%
		    gain = Math.min(gain, gainControl.getMaximum()); // Ensure the volume isn't set over the maximum
		    gainControl.setValue(gain);
		}
	    }
	});
	bar.add(soundUp);

	// 배경 음악 소리를 줄이는 버튼
	JButton soundDown = new JButton("sound-");
	soundDown.setFont(new Font("DungGeunMo", Font.PLAIN, 18));
	soundDown.setToolTipText("소리를 줄입니다.");
	soundDown.setOpaque(false);
	soundDown.setForeground(Color.white);
	soundDown.setFocusPainted(false);
	soundDown.setFocusable(false);
	soundDown.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
		    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		    float range = gainControl.getMaximum() - gainControl.getMinimum();
		    float gain = gainControl.getValue() - (range * 10 / 100); // Increase volume by 1%
		    gain = Math.max(gain, gainControl.getMinimum()); // Ensure the volume isn't set over the maximum
		    gainControl.setValue(gain);
		}
	    }
	});
	bar.add(soundDown);

	// 배경 음악을 멈추는 버튼
	JButton mute = new JButton("mute");
	mute.setFont(new Font("DungGeunMo", Font.PLAIN, 18));
	mute.setToolTipText("배경 음악을 음소거합니다.");
	mute.setOpaque(false);
	mute.setForeground(Color.white);
	mute.setFocusPainted(false);
	mute.setFocusable(false);
	mute.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		clip.stop();
	    }
	});
	bar.add(mute);

	// 게임 종료 버튼
	JButton exit = new JButton("exit");
	exit.setFont(new Font("DungGeunMo", Font.PLAIN, 18));
	exit.setToolTipText("게임을 종료합니다.");
	exit.setOpaque(false);
	exit.setForeground(Color.white);
	exit.setFocusPainted(false);
	exit.setFocusable(false);
	exit.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		int choose = JOptionPane.showConfirmDialog(null, "정말 종료하시겠습니까?", "게임 종료", JOptionPane.YES_NO_OPTION);
		if (choose == JOptionPane.YES_OPTION)
		    System.exit(0);
	    }
	});
	bar.add(exit);

	// 화면 상단에 고정
	bar.setFloatable(false);
	bar.setOpaque(false);
	add(bar, BorderLayout.NORTH);
    }

    // 사운드 무한 재생 메소드
    private void loadAudio() {
	try {
	    clip = AudioSystem.getClip();
	    File audioFile = new File("audio/startMusic.wav");
	    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
	    clip.open(audioStream);
	    clip.loop(Clip.LOOP_CONTINUOUSLY);
	} catch (LineUnavailableException e) {
	    e.printStackTrace();
	} catch (UnsupportedAudioFileException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    // 사운드 중단 메소드
    public void muteAudio() {
	clip.stop();
    }
}
