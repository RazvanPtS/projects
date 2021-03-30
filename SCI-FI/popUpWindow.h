#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_popUpWindow.h"

class popUpWindow : public QMainWindow
{
	Q_OBJECT
		friend class Gui;
public:
	popUpWindow(QWidget *parent=Q_NULLPTR);
	~popUpWindow();
private:
	Ui::MainWindow uiPopUp;
};
