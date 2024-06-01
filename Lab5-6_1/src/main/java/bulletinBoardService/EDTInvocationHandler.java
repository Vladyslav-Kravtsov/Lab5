package bulletinBoardService;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class EDTInvocationHandler extends JFrame implements InvocationHandler{
    private JTextField inputField;
    private JTextField groupField;
    private JTextField portField;
    private JTextField nameField;
    private JTextArea outputArea;
    private Object invocationResult = null;
    private UITasks ui;
    private MessangerImpl messanger;

    public EDTInvocationHandler(UITasks ui) {
        this.ui = ui;
    }
    public EDTInvocationHandler() {

    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args)
            throws Throwable {
        if (SwingUtilities.isEventDispatchThread()) {
            invocationResult = method.invoke(ui, args);
        } else {
            Runnable shell = new Runnable() {
                @Override
                public void run() {
                    try {
                        invocationResult = method.invoke(ui, args);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            };
            SwingUtilities.invokeAndWait(shell);
        }
        return invocationResult;
    }
    private class UITasksImpl implements UITasks {
        @Override
        public String getMessage() {
            String res = inputField.getText();
            inputField.setText("");
            return res;
        }
        @Override
        public void setText(String txt) {
            outputArea.append(txt + "\n");
        }
    }
    public void InitFrame() {
            // Встановлюємо заголовок вікна
            setTitle("Messanger");

            // Створюємо панель з полями вводу та кнопкою "Надіслати"
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout());
            inputField = new JTextField();
            JButton sendButton = new JButton("Надіслати");
            sendButton.addActionListener(arg0->{
                messanger.send();
            });
            topPanel.add(inputField, BorderLayout.CENTER);
            topPanel.add(sendButton, BorderLayout.EAST);

            // Створюємо панель з полями "Група", "Порт" та "Ім'я"
            JPanel leftPanel = new JPanel(new GridLayout(3,2));
            JLabel groupLabel = new JLabel("Група:");
            groupLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel portLabel = new JLabel("Порт:");
            portLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel nameLabel = new JLabel("Ім'я:");
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            groupField = new JTextField(10);
            portField = new JTextField(10);
            nameField = new JTextField(10);
            leftPanel.add(groupLabel);
            leftPanel.add(groupField);
            leftPanel.add(portLabel);
            leftPanel.add(portField);
            leftPanel.add(nameLabel);
            leftPanel.add(nameField);

            // Створюємо панель з великим полем виводу
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BorderLayout());
            outputArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(outputArea);
            rightPanel.add(scrollPane, BorderLayout.CENTER);

            // Створюємо панель з кнопками "З'єднати", "Роз'єднати", "Очистити" та "Завершити"
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            JButton connectButton = new JButton("З'єднати");
            connectButton.addActionListener(arg0->{
                UITasks ui = (UITasks) Proxy.newProxyInstance(getClass().getClassLoader(),
                        new Class[]{UITasks.class},
                        new EDTInvocationHandler(new UITasksImpl()));
                InetAddress addr = null;
                try {
                    addr = InetAddress.getByName(groupField.getText());
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
                messanger = new MessangerImpl(addr, Integer.parseInt(portField.getText()), nameField.getText(), ui);
                messanger.start();
            });
            JButton disconnectButton = new JButton("Роз'єднати");
            disconnectButton.addActionListener(arg0->{
                messanger.stop();
            });
            JButton clearButton = new JButton("Очистити");
            clearButton.addActionListener(arg0->{
                outputArea.setText("");
            });
            JButton exitButton = new JButton("Завершити");
            exitButton.addActionListener(arg0->{
                messanger.stop();
                dispose();
            });
            bottomPanel.add(connectButton);
            bottomPanel.add(disconnectButton);
            bottomPanel.add(clearButton);
            bottomPanel.add(exitButton);

            // Додаємо всі панелі до вікна
            add(topPanel, BorderLayout.NORTH);
            add(leftPanel, BorderLayout.WEST);
            add(rightPanel, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);

            // Встановлюємо розмір вікна та дозволяємо його закривати
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Показуємо вікно
            setVisible(true);
    }

    public static void main(String[] args) {
        EDTInvocationHandler edtInvocationHandler = new EDTInvocationHandler();
        edtInvocationHandler.InitFrame();
    }
}

