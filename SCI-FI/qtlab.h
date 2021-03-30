#pragma once
#include "ui_qtlab.h"
#include <QtWidgets/QMainWindow>
#include <QKeyEvent>
#include <qdebug.h>
class qtlab : public QMainWindow
{
	Q_OBJECT
		friend class Gui;
public:
	qtlab(QWidget* parent = Q_NULLPTR);
protected:
	void keyPressEvent(QKeyEvent* event) override
	{
		if (event->key() == Qt::Key_Z && Qt::KeyboardModifier::ControlModifier)
		{
			ui.undoButton->click();
		}
		else if (event->key() == Qt::Key_Y && Qt::KeyboardModifier::ControlModifier)
		{
			ui.redoButton->click();
		}
	}
private:
	Ui::qtlabClass ui;
};
