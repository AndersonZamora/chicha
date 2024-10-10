package com.example.lasesteras.view.loginView;

import com.example.lasesteras.model.employeesModel;

public class loginContract {

    public interface loginView {
        void saveUser(employeesModel model);
        void navigateToMainNoShape(employeesModel model);
        void navigateToMain();
    }

    public interface sharedPresenter {
        void attachViewLogin(loginView loginView);
    }
}
