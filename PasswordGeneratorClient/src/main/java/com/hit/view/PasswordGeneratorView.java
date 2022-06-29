package com.hit.view;

import com.hit.controller.PasswordGeneratorController;
import com.hit.dm.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.Optional;

public class PasswordGeneratorView {
    private final PasswordGeneratorController controller;
    private String loggedInUser = null;

    private JTextArea lblUsername;
    private JTextArea lblPassword;
    private JPasswordField txtPassword;
    private JTextField txtUsername;
    private JButton btnLogin;
    private JPanel panelMain;
    private JTabbedPane secretsPane;
    private JButton btnRegister;
    private JButton btnChangePw;
    private JButton btnDelUser;
    private JComboBox cmbLoginSecrets;
    private JTextPane secretNameTextPane;
    private JTextPane usernameTextPane;
    private JTextPane passwordTextPane;
    private JTextPane domainTextPane;
    private JTextPane remarkTextPane;
    private JTextPane ownerUsernameTextPane1;
    private JTextPane categoryTextPane1;
    private JTextField txtLoginSecretName;
    private JTextField txtLoginSecretUsername;
    private JTextField txtLoginSecretPassword;
    private JTextField txtLoginSecretDomain;
    private JTextField txtLoginSecretRemark;
    private JTextField txtLoginSecretOwnerUsername;
    private JButton btnUpdateLoginSecret;
    private JButton btnCreateLoginSecret;
    private JButton btnUpdateCardSecret;
    private JButton btnCreateCardSecret;
    private JTextPane expirationDayTextPane;
    private JTextField txtCardSecretExpDay;
    private JTextPane expirationMonthTextPane;
    private JTextPane expirationYearTextPane;
    private JTextField txtCardSecretExpYear;
    private JTextPane cardTypeTextPane;
    private JComboBox cmbCardSecretType;
    private JTextPane CVVTextPane;
    private JTextField txtCardSecretCvv;
    private JComboBox cmbIdentitySecretTitle;
    private JTextField txtIdentitySecretName;
    private JTextField txtCardSecretName;
    private JTextField txtCardSecretOwnerName;
    private JTextField txtCardSecretNumber;
    private JTextField txtCardSecretDomain;
    private JTextField txtCardSecretRemark;
    private JTextField txtCardSecretOwnerUsername;
    private JComboBox cmbCardSecrets;
    private JComboBox cmbIdentitySecrets;
    private JTextField txtIdentitySecretFirstName;
    private JTextField txtIdentitySecretMiddleName;
    private JTextField txtIdentitySecretLastName;
    private JTextField txtIdentitySecretCompany;
    private JTextField txtIdentitySecretLicense;
    private JTextField txtIdentitySecretPassport;
    private JTextField txtIdentitySecretEmail;
    private JTextField txtIdentitySecretPhone;
    private JTextField txtIdentitySecretAddress;
    private JTextField txtIdentitySecretPostal;
    private JTextField txtIdentitySecretDomain;
    private JTextField txtIdentitySecretRemark;
    private JTextField txtIdentitySecretOwnerUsername;
    private JButton btnUpdateIdentitySecret;
    private JButton btnCreateIdentitySecret;
    private JTextField txtTextSecretName;
    private JTextArea txtTextSecretText;
    private JTextField txtTextSecretDomain;
    private JTextField txtTextSecretRemark;
    private JTextField txtTextSecretOwnerUsername;
    private JButton btnUpdateTextSecret;
    private JButton btnCreateTextSecret;
    private JComboBox cmbTextSecretCategory;
    private JComboBox cmbLoginSecretCategory;
    private JComboBox cmbCardSecretCategory;
    private JComboBox cmbIdentitySecretCategory;
    private JComboBox cmbTextSecrets;
    private JComboBox cmbCardSecretExpMonth;
    private JButton btnDeleteLoginSecret;
    private JButton btnDeleteCardSecret;
    private JButton btnDeleteIdentitySecret;
    private JButton btnDeleteTextSecret;

    public PasswordGeneratorView() {
        controller = new PasswordGeneratorController();

        btnLogin.addActionListener(e -> {
            if (loggedInUser != null) {
                setLoggedInUi(false);
                JOptionPane.showMessageDialog(null, "You were logged out.");
                return;
            }

            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            boolean success;
            try {
                success = controller.login(username, password);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                JOptionPane.showMessageDialog(null, "Login success!");
                controller.setPassword(password);
                setLoggedInUi(true);
                prepareUiGeneralData();
                populateSecrets();
            }
            else {
                JOptionPane.showMessageDialog(null, "Incorrect username or password.");
            }
        });

        btnRegister.addActionListener(e -> {
            String username = txtUsername.getText();

            // Field validation (Also exception-validated in server side, but we didn't have time
            // to implement exception forwarding between client and server. Sorry :( )
            if (username == null || username.length() < 5) {
                JOptionPane.showMessageDialog(null, "The chosen username is too short");
                return;
            }
            if (username.length() > 15) {
                JOptionPane.showMessageDialog(null, "The chosen username is too long");
                return;
            }

            String password = new String(txtPassword.getPassword());

            // Password validation
            if (password.length() < 8) {
                JOptionPane.showMessageDialog(null, "The chosen password is too short.");
                return;
            }
            if (password.length() > 20) {
                JOptionPane.showMessageDialog(null, "The chosen password is too long.");
                return;
            }

            boolean success;
            try {
                success = controller.register(username, password);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                JOptionPane.showMessageDialog(null, "User "+username+" created successfully! Please login.");
            }
            else {
                JOptionPane.showMessageDialog(null, "Error! Username is already taken, or password was not valid.");
            }
        });

        btnChangePw.addActionListener(e -> {

            try {
                // Since the user's password is used as a secret key for symmetric encryption,
                // Changing the password will mean that all of the secrets need to be re-encrypted
                // Sorry! We had not enough time to fully implement this :(
                // Workaround is to make sure the user has no secrets available.
                if (controller.getSecrets(loggedInUser).size() > 0) {
                    JOptionPane.showMessageDialog(null, "Can't change password while you have secrets.\n" +
                            "Please write them down and delete them, then try to change password.");
                    return;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to check current number of secrets. " + ex.getMessage());
                return;
            }

            String password = new String(txtPassword.getPassword());

            boolean success;
            try {
                success = controller.changePassword(loggedInUser, password);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                setLoggedInUi(false);
                JOptionPane.showMessageDialog(null, "Your password was changed. Please login with your new password");
            }
            else {
                JOptionPane.showMessageDialog(null, "An invalid new password was given");
            }
        });

        btnDelUser.addActionListener(e -> {
            boolean success;
            try {
                success = controller.unregister(loggedInUser);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                setLoggedInUi(false);
                JOptionPane.showMessageDialog(null, "User is now deleted. You were logged out.");
            }
            else {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to delete the user.");
            }
        });

        btnUpdateLoginSecret.addActionListener(e -> {
            String domain = txtLoginSecretDomain.getText();
            String secretName = txtLoginSecretName.getText();
            String remark = txtLoginSecretRemark.getText();

            SecretCategory category;
            try {
                category = SecretCategory.valueOf(cmbLoginSecretCategory.getSelectedItem().toString());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to parse selected category " + ex.getMessage());
                return;
            }

            String secretUsername = txtLoginSecretUsername.getText();
            String secretPassword = txtLoginSecretPassword.getText();

            String originalSecretName = cmbLoginSecrets.getSelectedItem().toString();

            boolean success;
            try {
                success = controller.updateLoginSecret(loggedInUser, domain, secretName, category, remark,
                        secretUsername, secretPassword, originalSecretName);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                JOptionPane.showMessageDialog(null, "Secret was updated.");
            }
            else {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to update the secret.");
            }
            int selectedIdx = cmbLoginSecrets.getSelectedIndex();
            cmbLoginSecrets.addItem(secretName);
            cmbLoginSecrets.setSelectedItem(secretName);
            cmbLoginSecrets.removeItemAt(selectedIdx);
        });

        btnCreateLoginSecret.addActionListener(e -> {
            String domain = txtLoginSecretDomain.getText();
            String secretName = txtLoginSecretName.getText();
            String remark = txtLoginSecretRemark.getText();

            if (!checkUniqueSecretName(secretName)) return;

            SecretCategory category;
            try {
                category = SecretCategory.valueOf(cmbLoginSecretCategory.getSelectedItem().toString());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to parse selected category " + ex.getMessage());
                return;
            }

            String secretUsername = txtLoginSecretUsername.getText();
            String secretPassword = txtLoginSecretPassword.getText();

            boolean success;
            try {
                success = controller.addLoginSecret(loggedInUser, domain, secretName, category, remark,
                        secretUsername, secretPassword);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                JOptionPane.showMessageDialog(null, "Secret was created.");
            }
            else {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to update the secret.");
            }

            cmbLoginSecrets.addItem(secretName);
            cmbLoginSecrets.setSelectedItem(secretName);
        });

        btnUpdateCardSecret.addActionListener(e -> {
            String domain = txtCardSecretDomain.getText();
            String secretName = txtCardSecretName.getText();
            String remark = txtCardSecretRemark.getText();

            SecretCategory category;
            try {
                category = SecretCategory.valueOf(cmbCardSecretCategory.getSelectedItem().toString());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to parse selected category " + ex.getMessage());
                return;
            }

            String ownerName = txtCardSecretOwnerName.getText();
            long cardNumber;
            byte cardExpDay;
            short cardExpYear;
            short cvv;
            try {
                cardNumber = Long.parseLong(txtCardSecretNumber.getText());
                cardExpDay = Byte.parseByte(txtCardSecretExpDay.getText());
                cardExpYear = Short.parseShort(txtCardSecretExpYear.getText());
                cvv = Short.parseShort(txtCardSecretCvv.getText());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "A numeric field was not in the correct format "
                        + ex.getMessage());
                return;
            }
            Month cardExpMonth = Month.valueOf(cmbCardSecretExpMonth.getSelectedItem().toString());
            CardType cardType = CardType.valueOf(cmbCardSecretType.getSelectedItem().toString());

            boolean success;
            try {
                success = controller.updateCardSecret(loggedInUser, domain, secretName, category, remark, ownerName,
                        cardNumber, cardExpDay, cardExpMonth, cardExpYear, cardType, cvv, cmbCardSecrets.getSelectedItem().toString());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                JOptionPane.showMessageDialog(null, "Secret was updated.");
            }
            else {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to update the secret.");
            }

            int selectedIdx = cmbCardSecrets.getSelectedIndex();
            cmbCardSecrets.addItem(secretName);
            cmbCardSecrets.setSelectedItem(secretName);
            cmbCardSecrets.removeItemAt(selectedIdx);
        });

        btnCreateCardSecret.addActionListener(e -> {
            String domain = txtCardSecretDomain.getText();
            String secretName = txtCardSecretName.getText();
            String remark = txtCardSecretRemark.getText();

            if (!checkUniqueSecretName(secretName)) return;

            SecretCategory category;
            try {
                category = SecretCategory.valueOf(cmbCardSecretCategory.getSelectedItem().toString());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to parse selected category " + ex.getMessage());
                return;
            }

            String ownerName = txtCardSecretOwnerName.getText();
            long cardNumber;
            byte cardExpDay;
            short cardExpYear;
            short cvv;
            try {
                 cardNumber = Long.parseLong(txtCardSecretNumber.getText());
                 cardExpDay = Byte.parseByte(txtCardSecretExpDay.getText());
                 cardExpYear = Short.parseShort(txtCardSecretExpYear.getText());
                 cvv = Short.parseShort(txtCardSecretCvv.getText());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "A numeric field was not in the correct format "
                        + ex.getMessage());
                return;
            }
            Month cardExpMonth = Month.valueOf(cmbCardSecretExpMonth.getSelectedItem().toString());
            CardType cardType = CardType.valueOf(cmbCardSecretType.getSelectedItem().toString());

            boolean success;
            try {
                success = controller.addCardSecret(loggedInUser, domain, secretName, category, remark, ownerName,
                        cardNumber, cardExpDay, cardExpMonth, cardExpYear, cardType, cvv);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                JOptionPane.showMessageDialog(null, "Secret was created.");
            }
            else {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to update the secret.");
            }

            cmbCardSecrets.addItem(secretName);
            cmbCardSecrets.setSelectedItem(secretName);
        });

        btnUpdateIdentitySecret.addActionListener(e -> {
            String domain = txtIdentitySecretDomain.getText();
            String secretName = txtIdentitySecretName.getText();
            String remark = txtIdentitySecretRemark.getText();

            SecretCategory category;
            try {
                category = SecretCategory.valueOf(cmbIdentitySecretCategory.getSelectedItem().toString());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to parse selected category " + ex.getMessage());
                return;
            }

            String firstName = txtIdentitySecretFirstName.getText();
            String middleName = txtIdentitySecretMiddleName.getText();
            String lastName = txtIdentitySecretLastName.getText();
            IdentityType idType = IdentityType.valueOf(cmbIdentitySecretTitle.getSelectedItem().toString());
            String company = txtIdentitySecretCompany.getText();

            long license;
            long passport;
            int postal;
            try {
                license = Long.parseLong(txtIdentitySecretLicense.getText());
                passport = Long.parseLong(txtIdentitySecretPassport.getText());
                postal = Integer.parseInt(txtIdentitySecretPostal.getText());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "A numeric field was not in the correct format "
                        + ex.getMessage());
                return;
            }

            String email = txtIdentitySecretEmail.getText();
            String phone = txtIdentitySecretPhone.getText();
            String address = txtIdentitySecretAddress.getText();
            String originalSecretName = cmbIdentitySecrets.getSelectedItem().toString();

            boolean success;
            try {
                success = controller.updateIdentitySecret(loggedInUser, domain, secretName, category, remark,firstName,
                        middleName, lastName, idType, company, license, passport, email, phone, address, postal, originalSecretName);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                JOptionPane.showMessageDialog(null, "Secret was updated.");
            }
            else {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to update the secret.");
            }

            int selectedIdx = cmbIdentitySecrets.getSelectedIndex();
            cmbIdentitySecrets.addItem(secretName);
            cmbIdentitySecrets.setSelectedItem(secretName);
            cmbIdentitySecrets.removeItemAt(selectedIdx);
        });

        btnCreateIdentitySecret.addActionListener(e -> {
            String domain = txtIdentitySecretDomain.getText();
            String secretName = txtIdentitySecretName.getText();
            String remark = txtIdentitySecretRemark.getText();

            if (!checkUniqueSecretName(secretName)) return;

            SecretCategory category;
            try {
                category = SecretCategory.valueOf(cmbIdentitySecretCategory.getSelectedItem().toString());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to parse selected category " + ex.getMessage());
                return;
            }

            String firstName = txtIdentitySecretFirstName.getText();
            String middleName = txtIdentitySecretMiddleName.getText();
            String lastName = txtIdentitySecretLastName.getText();
            IdentityType idType = IdentityType.valueOf(cmbIdentitySecretTitle.getSelectedItem().toString());
            String company = txtIdentitySecretCompany.getText();

            long license;
            long passport;
            int postal;
            try {
                license = Long.parseLong(txtIdentitySecretLicense.getText());
                passport = Long.parseLong(txtIdentitySecretPassport.getText());
                postal = Integer.parseInt(txtIdentitySecretPostal.getText());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "A numeric field was not in the correct format "
                        + ex.getMessage());
                return;
            }

            String email = txtIdentitySecretEmail.getText();
            String phone = txtIdentitySecretPhone.getText();
            String address = txtIdentitySecretAddress.getText();

            boolean success;
            try {
                success = controller.addIdentitySecret(loggedInUser, domain, secretName, category, remark,firstName,
                        middleName, lastName, idType, company, license, passport, email, phone, address, postal);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                JOptionPane.showMessageDialog(null, "Secret was created.");
            }
            else {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to update the secret.");
            }

            cmbIdentitySecrets.addItem(secretName);
            cmbIdentitySecrets.setSelectedItem(secretName);
        });

        cmbLoginSecrets.addItemListener(e -> {
            if (loggedInUser == null) return;
            if (e != null && e.getItem() != null && e.getItem().toString() != null) {
                ArrayList<AbstractSecretDTO> secrets = null;
                try {
                    secrets = controller.getSecrets(loggedInUser);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to get secrets list. " + ex.getMessage());
                }
                Optional<AbstractSecretDTO> selectedSecret = secrets.stream().filter(x ->
                        x.getSecretName().equals(e.getItem().toString())).findFirst();
                if (selectedSecret.isPresent()) {
                    LoginSecret loginSecret = (LoginSecret)selectedSecret.get();
                    txtLoginSecretDomain.setText(loginSecret.getDomain());
                    txtLoginSecretName.setText(loginSecret.getSecretName());
                    txtLoginSecretPassword.setText(loginSecret.getPassword());
                    txtLoginSecretRemark.setText(loginSecret.getRemark());
                    txtLoginSecretUsername.setText(loginSecret.getUserName());
                    cmbLoginSecretCategory.setSelectedItem(loginSecret.getSecretCategory().name());
                }

            }
        });

        btnUpdateTextSecret.addActionListener(e -> {
            String domain = txtTextSecretDomain.getText();
            String secretName = txtTextSecretName.getText();
            String remark = txtTextSecretRemark.getText();

            SecretCategory category;
            try {
                category = SecretCategory.valueOf(cmbTextSecretCategory.getSelectedItem().toString());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to parse selected category " + ex.getMessage());
                return;
            }

            String content = txtTextSecretText.getText();
            String originalSecretName = cmbTextSecrets.getSelectedItem().toString();

            boolean success;
            try {
                success = controller.updateTextSecret(loggedInUser, domain, secretName, category, remark, content, originalSecretName);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                JOptionPane.showMessageDialog(null, "Secret was updated.");
            }
            else {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to update the secret.");
            }

            int selectedIdx = cmbTextSecrets.getSelectedIndex();
            cmbTextSecrets.addItem(secretName);
            cmbTextSecrets.setSelectedItem(secretName);
            cmbTextSecrets.removeItemAt(selectedIdx);
        });

        btnCreateTextSecret.addActionListener(e -> {
            String domain = txtTextSecretDomain.getText();
            String secretName = txtTextSecretName.getText();
            String remark = txtTextSecretRemark.getText();

            if (!checkUniqueSecretName(secretName)) return;

            SecretCategory category;
            try {
                category = SecretCategory.valueOf(cmbTextSecretCategory.getSelectedItem().toString());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to parse selected category " + ex.getMessage());
                return;
            }

            String content = txtTextSecretText.getText();

            boolean success;
            try {
                success = controller.addTextSecret(loggedInUser, domain, secretName, category, remark, content);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to server. " + ex.getMessage());
                return;
            }
            if (success) {
                JOptionPane.showMessageDialog(null, "Secret was created.");
            }
            else {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to update the secret.");
            }

            cmbTextSecrets.addItem(secretName);
            cmbTextSecrets.setSelectedItem(secretName);
        });

        cmbCardSecrets.addItemListener(e -> {
            if (loggedInUser == null) return;
            if (e != null && e.getItem() != null && e.getItem().toString() != null) {
                ArrayList<AbstractSecretDTO> secrets = null;
                try {
                    secrets = controller.getSecrets(loggedInUser);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to get secrets list. " + ex.getMessage());
                }
                Optional<AbstractSecretDTO> selectedSecret = secrets.stream().filter(x ->
                        x.getSecretName().equals(e.getItem().toString())).findFirst();
                if (selectedSecret.isPresent()) {
                    CardSecret cardSecret = (CardSecret)selectedSecret.get();
                    txtCardSecretName.setText(cardSecret.getSecretName());
                    txtCardSecretOwnerName.setText(cardSecret.getCardOwnerName());
                    txtCardSecretNumber.setText(String.valueOf(cardSecret.getCardNumber()));
                    txtCardSecretExpDay.setText(String.valueOf(cardSecret.getExpirationDay()));
                    cmbCardSecretExpMonth.setSelectedItem(String.valueOf(cardSecret.getExpirationMonth()));
                    txtCardSecretExpYear.setText(String.valueOf(cardSecret.getExpirationYear()));
                    cmbCardSecretType.getModel().setSelectedItem(String.valueOf(cardSecret.getCardType()));
                    txtCardSecretCvv.setText(String.valueOf(cardSecret.getCvv()));
                    txtCardSecretDomain.setText(String.valueOf(cardSecret.getDomain()));
                    txtCardSecretRemark.setText(String.valueOf(cardSecret.getRemark()));
                    cmbCardSecretCategory.getModel().setSelectedItem(String.valueOf(cardSecret.getSecretCategory()));
                }
            }
        });

        cmbIdentitySecrets.addItemListener(e -> {
            if (loggedInUser == null) return;
            if (e != null && e.getItem() != null && e.getItem().toString() != null) {
                ArrayList<AbstractSecretDTO> secrets = null;
                try {
                    secrets = controller.getSecrets(loggedInUser);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to get secrets list. " + ex.getMessage());
                }
                Optional<AbstractSecretDTO> selectedSecret = secrets.stream().filter(x ->
                        x.getSecretName().equals(e.getItem().toString())).findFirst();
                if (selectedSecret.isPresent()) {
                    IdentitySecret idSecret = (IdentitySecret) selectedSecret.get();
                    txtIdentitySecretName.setText(idSecret.getSecretName());
                    txtIdentitySecretFirstName.setText(idSecret.getFirstName());
                    txtIdentitySecretMiddleName.setText(idSecret.getMiddleName());
                    txtIdentitySecretLastName.setText(idSecret.getLastName());
                    cmbIdentitySecretTitle.setSelectedItem(idSecret.getIdentityType().name());
                    txtIdentitySecretCompany.setText(idSecret.getCompany());
                    txtIdentitySecretLicense.setText(String.valueOf(idSecret.getLicenseNumber()));
                    txtIdentitySecretPassport.setText(String.valueOf(idSecret.getPassportNumber()));
                    txtIdentitySecretEmail.setText(idSecret.getEmail());
                    txtIdentitySecretPhone.setText(idSecret.getPhoneNumber());
                    txtIdentitySecretAddress.setText(idSecret.getAddress());
                    txtIdentitySecretPostal.setText(String.valueOf(idSecret.getPostalCode()));
                    txtIdentitySecretDomain.setText(idSecret.getDomain());
                    txtIdentitySecretRemark.setText(idSecret.getRemark());
                    cmbIdentitySecretCategory.setSelectedItem(idSecret.getSecretCategory().name());
                }
            }
        });

        cmbTextSecrets.addItemListener(e -> {
            if (loggedInUser == null) return;
            if (e != null && e.getItem() != null && e.getItem().toString() != null) {
                ArrayList<AbstractSecretDTO> secrets = null;
                try {
                    secrets = controller.getSecrets(loggedInUser);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to get secrets list. " + ex.getMessage());
                }
                Optional<AbstractSecretDTO> selectedSecret = secrets.stream().filter(x ->
                        x.getSecretName().equals(e.getItem().toString())).findFirst();
                if (selectedSecret.isPresent()) {
                    TextSecret txtSecret = (TextSecret) selectedSecret.get();
                    txtTextSecretName.setText(txtSecret.getSecretName());
                    txtTextSecretText.setText(txtSecret.getContent());
                    txtTextSecretDomain.setText(txtSecret.getDomain());
                    txtTextSecretRemark.setText(txtSecret.getRemark());
                    cmbTextSecretCategory.setSelectedItem(txtSecret.getSecretCategory().name());
                }
            }
        });
        btnDeleteLoginSecret.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cmbLoginSecrets.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "No secret was selected");
                    return;
                }
                String secretName = cmbLoginSecrets.getSelectedItem().toString();
                try {
                    if (controller.removeSecret(loggedInUser, secretName))
                        JOptionPane.showMessageDialog(null, "Secret "+secretName+" was deleted.");
                    else {
                        JOptionPane.showMessageDialog(null, "Cannot delete selected secret.");
                        return;
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to delete secret." + ex.getMessage());
                    return;
                }

                // Update UI
                int secretCount = cmbLoginSecrets.getItemCount();
                if (secretCount > 1) {
                    // Select a different secret
                    int newSelectedIdx = 0;
                    if (cmbLoginSecrets.getSelectedIndex() == 0)
                        newSelectedIdx = 1;
                    cmbLoginSecrets.setSelectedIndex(newSelectedIdx);
                    cmbLoginSecrets.removeItem(secretName);
                }
                else {
                    // Removing the only available secret - clear the screen.
                    clearLoginText();
                    cmbLoginSecrets.removeAllItems();
                }
            }
        });
        btnDeleteCardSecret.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cmbCardSecrets.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "No secret was selected");
                    return;
                }
                String secretName = cmbCardSecrets.getSelectedItem().toString();
                try {
                    if (controller.removeSecret(loggedInUser, secretName))
                        JOptionPane.showMessageDialog(null, "Secret "+secretName+" was deleted.");
                    else {
                        JOptionPane.showMessageDialog(null, "Cannot delete selected secret.");
                        return;
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to delete secret." + ex.getMessage());
                    return;
                }

                // Update UI
                int secretCount = cmbCardSecrets.getItemCount();
                if (secretCount > 1) {
                    // Select a different secret
                    int newSelectedIdx = 0;
                    if (cmbCardSecrets.getSelectedIndex() == 0)
                        newSelectedIdx = 1;
                    cmbCardSecrets.setSelectedIndex(newSelectedIdx);
                    cmbCardSecrets.removeItem(secretName);
                }
                else {
                    // Removing the only available secret - clear the screen.
                    clearCardText();
                    cmbCardSecrets.removeAllItems();
                }
            }
        });
        btnDeleteIdentitySecret.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cmbIdentitySecrets.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "No secret was selected");
                    return;
                }
                String secretName = cmbIdentitySecrets.getSelectedItem().toString();
                try {
                    if (controller.removeSecret(loggedInUser, secretName))
                        JOptionPane.showMessageDialog(null, "Secret "+secretName+" was deleted.");
                    else {
                        JOptionPane.showMessageDialog(null, "Cannot delete selected secret.");
                        return;
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to delete secret." + ex.getMessage());
                    return;
                }

                // Update UI
                int secretCount = cmbIdentitySecrets.getItemCount();
                if (secretCount > 1) {
                    // Select a different secret
                    int newSelectedIdx = 0;
                    if (cmbIdentitySecrets.getSelectedIndex() == 0)
                        newSelectedIdx = 1;
                    cmbIdentitySecrets.setSelectedIndex(newSelectedIdx);
                    cmbIdentitySecrets.removeItem(secretName);
                }
                else {
                    // Removing the only available secret - clear the screen.
                    clearIdText();
                    cmbIdentitySecrets.removeAllItems();
                }
            }
        });
        btnDeleteTextSecret.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cmbTextSecrets.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "No secret was selected");
                    return;
                }
                String secretName = cmbTextSecrets.getSelectedItem().toString();
                try {
                    if (controller.removeSecret(loggedInUser, secretName))
                        JOptionPane.showMessageDialog(null, "Secret "+secretName+" was deleted.");
                    else {
                        JOptionPane.showMessageDialog(null, "Cannot delete selected secret.");
                        return;
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to delete secret." + ex.getMessage());
                    return;
                }

                // Update UI
                int secretCount = cmbTextSecrets.getItemCount();
                if (secretCount > 1) {
                    // Select a different secret
                    int newSelectedIdx = 0;
                    if (cmbTextSecrets.getSelectedIndex() == 0)
                        newSelectedIdx = 1;
                    cmbTextSecrets.setSelectedIndex(newSelectedIdx);
                    cmbTextSecrets.removeItem(secretName);
                }
                else {
                    // Removing the only available secret - clear the screen.
                    clearTextText();
                    cmbTextSecrets.removeAllItems();
                }
            }
        });
    }

    private void prepareUiGeneralData() {
        cmbLoginSecrets.removeAllItems();
        cmbCardSecrets.removeAllItems();
        cmbIdentitySecrets.removeAllItems();
        cmbTextSecretCategory.removeAllItems();
        cmbLoginSecretCategory.removeAllItems();
        cmbCardSecretCategory.removeAllItems();
        cmbIdentitySecretCategory.removeAllItems();
        cmbTextSecretCategory.removeAllItems();
        cmbCardSecretType.removeAllItems();
        cmbIdentitySecretTitle.removeAllItems();

        for (SecretCategory cat : SecretCategory.values()) {
            cmbLoginSecretCategory.addItem(cat.name());
            cmbCardSecretCategory.addItem(cat.name());
            cmbIdentitySecretCategory.addItem(cat.name());
            cmbTextSecretCategory.addItem(cat.name());
        }

        for (CardType cardType : CardType.values()) {
            cmbCardSecretType.addItem(cardType.name());
        }

        for (IdentityType idType : IdentityType.values()) {
            cmbIdentitySecretTitle.addItem(idType.name());
        }

        for (Month month : Month.values()) {
            cmbCardSecretExpMonth.addItem(month.name());
        }

        txtLoginSecretOwnerUsername.setText(loggedInUser);
        txtCardSecretOwnerUsername.setText(loggedInUser);
        txtIdentitySecretOwnerUsername.setText(loggedInUser);
        txtTextSecretOwnerUsername.setText(loggedInUser);
    }

    private void setLoggedInUi(boolean isLoggedIn) {
        if (isLoggedIn) {
            loggedInUser = txtUsername.getText();
            // Enable actions for logged-in users
            btnLogin.setText("Logout");
            btnRegister.setEnabled(false);
            btnChangePw.setEnabled(true);
            btnDelUser.setEnabled(true);
            secretsPane.setEnabled(true);
            btnCreateCardSecret.setEnabled(true);
            btnCreateIdentitySecret.setEnabled(true);
            btnCreateLoginSecret.setEnabled(true);
            btnCreateTextSecret.setEnabled(true);
            btnUpdateCardSecret.setEnabled(true);
            btnUpdateIdentitySecret.setEnabled(true);
            btnUpdateLoginSecret.setEnabled(true);
            btnUpdateTextSecret.setEnabled(true);
            btnDeleteCardSecret.setEnabled(true);
            btnDeleteIdentitySecret.setEnabled(true);
            btnDeleteLoginSecret.setEnabled(true);
            btnDeleteTextSecret.setEnabled(true);
            enableSecrets();
        }
        else {
            loggedInUser = null;
            // Enable actions for logged-out users
            btnLogin.setText("Login");
            btnRegister.setEnabled(true);
            btnChangePw.setEnabled(false);
            btnDelUser.setEnabled(false);
            secretsPane.setEnabled(false);
            btnCreateCardSecret.setEnabled(false);
            btnCreateIdentitySecret.setEnabled(false);
            btnCreateLoginSecret.setEnabled(false);
            btnCreateTextSecret.setEnabled(false);
            btnUpdateCardSecret.setEnabled(false);
            btnUpdateIdentitySecret.setEnabled(false);
            btnUpdateLoginSecret.setEnabled(false);
            btnUpdateTextSecret.setEnabled(false);
            btnDeleteCardSecret.setEnabled(false);
            btnDeleteIdentitySecret.setEnabled(false);
            btnDeleteLoginSecret.setEnabled(false);
            btnDeleteTextSecret.setEnabled(false);
            clearSecrets();
        }
    }

    private void clearSecrets() {
        clearLoginText();
        txtLoginSecretName.setEditable(false);
        txtLoginSecretUsername.setEditable(false);
        txtLoginSecretPassword.setEditable(false);
        txtLoginSecretDomain.setEditable(false);
        txtLoginSecretRemark.setEditable(false);
        txtLoginSecretOwnerUsername.setText("");
        txtLoginSecretOwnerUsername.setEditable(false);
        clearCardText();
        txtCardSecretExpDay.setEditable(false);
        txtCardSecretExpYear.setEditable(false);
        txtCardSecretCvv.setEditable(false);
        txtCardSecretName.setEditable(false);
        txtCardSecretOwnerName.setEditable(false);
        txtCardSecretNumber.setEditable(false);
        txtCardSecretDomain.setEditable(false);
        txtCardSecretRemark.setEditable(false);
        txtCardSecretOwnerUsername.setText("");
        txtCardSecretOwnerUsername.setEditable(false);
        clearIdText();
        txtIdentitySecretName.setEditable(false);
        txtIdentitySecretFirstName.setEditable(false);
        txtIdentitySecretMiddleName.setEditable(false);
        txtIdentitySecretLastName.setEditable(false);
        txtIdentitySecretCompany.setEditable(false);
        txtIdentitySecretLicense.setEditable(false);
        txtIdentitySecretPassport.setEditable(false);
        txtIdentitySecretEmail.setEditable(false);
        txtIdentitySecretPhone.setEditable(false);
        txtIdentitySecretAddress.setEditable(false);
        txtIdentitySecretPostal.setEditable(false);
        txtIdentitySecretDomain.setEditable(false);
        txtIdentitySecretRemark.setEditable(false);
        txtIdentitySecretOwnerUsername.setText("");
        txtIdentitySecretOwnerUsername.setEditable(false);
        clearTextText();
        txtTextSecretName.setEditable(false);
        txtTextSecretText.setEditable(false);
        txtTextSecretDomain.setEditable(false);
        txtTextSecretRemark.setEditable(false);
        txtTextSecretOwnerUsername.setText("");
        txtTextSecretOwnerUsername.setEditable(false);
        cmbCardSecrets.removeAllItems();
        cmbLoginSecrets.removeAllItems();
        cmbIdentitySecrets.removeAllItems();
        cmbTextSecrets.removeAllItems();
    }

    private void clearLoginText() {
        txtLoginSecretName.setText("");
        txtLoginSecretUsername.setText("");
        txtLoginSecretPassword.setText("");
        txtLoginSecretDomain.setText("");
        txtLoginSecretRemark.setText("");
    }

    private void clearCardText() {
        txtCardSecretExpDay.setText("");
        txtCardSecretExpYear.setText("");
        txtCardSecretCvv.setText("");
        txtCardSecretName.setText("");
        txtCardSecretOwnerName.setText("");
        txtCardSecretNumber.setText("");
        txtCardSecretDomain.setText("");
        txtCardSecretRemark.setText("");
    }

    private void clearIdText() {
        txtIdentitySecretName.setText("");
        txtIdentitySecretFirstName.setText("");
        txtIdentitySecretMiddleName.setText("");
        txtIdentitySecretLastName.setText("");
        txtIdentitySecretCompany.setText("");
        txtIdentitySecretLicense.setText("");
        txtIdentitySecretPassport.setText("");
        txtIdentitySecretEmail.setText("");
        txtIdentitySecretPhone.setText("");
        txtIdentitySecretAddress.setText("");
        txtIdentitySecretPostal.setText("");
        txtIdentitySecretDomain.setText("");
        txtIdentitySecretRemark.setText("");
    }

    private void clearTextText() {
        txtTextSecretName.setText("");
        txtTextSecretText.setText("");
        txtTextSecretDomain.setText("");
        txtTextSecretRemark.setText("");
    }

    private void enableSecrets() {
        txtLoginSecretName.setEditable(true);
        txtLoginSecretUsername.setEditable(true);
        txtLoginSecretPassword.setEditable(true);
        txtLoginSecretDomain.setEditable(true);
        txtLoginSecretRemark.setEditable(true);
        txtLoginSecretOwnerUsername.setEditable(true);
        txtCardSecretExpDay.setEditable(true);
        txtCardSecretExpYear.setEditable(true);
        txtCardSecretCvv.setEditable(true);
        txtIdentitySecretName.setEditable(true);
        txtCardSecretName.setEditable(true);
        txtCardSecretOwnerName.setEditable(true);
        txtCardSecretNumber.setEditable(true);
        txtCardSecretDomain.setEditable(true);
        txtCardSecretRemark.setEditable(true);
        txtCardSecretOwnerUsername.setEditable(true);
        txtIdentitySecretFirstName.setEditable(true);
        txtIdentitySecretMiddleName.setEditable(true);
        txtIdentitySecretLastName.setEditable(true);
        txtIdentitySecretCompany.setEditable(true);
        txtIdentitySecretLicense.setEditable(true);
        txtIdentitySecretPassport.setEditable(true);
        txtIdentitySecretEmail.setEditable(true);
        txtIdentitySecretPhone.setEditable(true);
        txtIdentitySecretAddress.setEditable(true);
        txtIdentitySecretPostal.setEditable(true);
        txtIdentitySecretPostal.setEditable(true);
        txtIdentitySecretDomain.setEditable(true);
        txtIdentitySecretRemark.setEditable(true);
        txtIdentitySecretOwnerUsername.setEditable(true);
        txtIdentitySecretOwnerUsername.setEditable(true);
        txtTextSecretName.setEditable(true);
        txtTextSecretText.setEditable(true);
        txtTextSecretDomain.setEditable(true);
        txtTextSecretRemark.setEditable(true);
        txtTextSecretOwnerUsername.setEditable(true);
    }

    private void populateSecrets() {
        ArrayList<AbstractSecretDTO> secrets;
        try {
             secrets = controller.getSecrets(loggedInUser);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Failed to fetch secrets list for logged in user. " + ex.getMessage());
            return;
        }

        for (AbstractSecretDTO secret : secrets) {
            if (secret.type.equals(CardSecret.class.getName())) {
                CardSecret cardSecret = (CardSecret)secret;
                cmbCardSecrets.addItem(cardSecret.getSecretName());
            }
            if (secret.type.equals(IdentitySecret.class.getName())) {
                IdentitySecret idSecret = (IdentitySecret)secret;
                cmbIdentitySecrets.addItem(idSecret.getSecretName());
            }
            if (secret.type.equals(LoginSecret.class.getName())) {
                LoginSecret loginSecret = (LoginSecret)secret;
                cmbLoginSecrets.addItem(loginSecret.getSecretName());
            }
            if (secret.type.equals(TextSecret.class.getName())) {
                TextSecret txtSecret = (TextSecret)secret;
                cmbTextSecrets.addItem(txtSecret.getSecretName());
            }
        }
    }

    private boolean checkUniqueSecretName(String secretName) {
        try {
            for (AbstractSecretDTO secret : controller.getSecrets(loggedInUser)) {
                if (secret.getSecretName().equals(secretName)) {
                    JOptionPane.showMessageDialog(null, "A secret with that name already exists!");
                    return false;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Failed validation of unique secret name: "
                    + ex.getMessage());
        }
        return true;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Password Generator");
        frame.setContentPane(new PasswordGeneratorView().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
