"""
PyQt6-based GUI widgets for IRCamera PC Controller

This module contains all the user interface widgets for the PC Controller
application, providing device management, network control, and system
integration.
"""

# Placeholder widgets with generic icons - GUI functionality under development
# Icons sourced from Android drawable resources for visual consistency

from PyQt6.QtCore import pyqtSignal, Qt
from PyQt6.QtWidgets import QLabel, QWidget, QVBoxLayout, QHBoxLayout
from PyQt6.QtGui import QPixmap, QPainter, QPen, QBrush


class GenericIconLabel(QLabel):
    """Label with generic settings gear icon for placeholder widgets."""
    
    def __init__(self, text: str, icon_type: str = "settings"):
        super().__init__()
        self.text_content = text
        self.icon_type = icon_type
        self._create_placeholder_content()
    
    def _create_placeholder_content(self):
        """Create content with generic icon and text."""
        # Create a simple pixmap with generic icon
        pixmap = QPixmap(200, 60)
        pixmap.fill(Qt.GlobalColor.lightGray)
        
        painter = QPainter(pixmap)
        painter.setRenderHint(QPainter.RenderHint.Antialiasing)
        
        # Draw icon based on type
        if self.icon_type == "settings":
            self._draw_settings_icon(painter, 10, 10, 40)
        elif self.icon_type == "calibration":
            self._draw_calibration_icon(painter, 10, 10, 40)
        else:
            self._draw_generic_icon(painter, 10, 10, 40)
        
        # Draw text
        painter.setPen(QPen(Qt.GlobalColor.black))
        painter.drawText(60, 35, self.text_content)
        painter.end()
        
        self.setPixmap(pixmap)
    
    def _draw_settings_icon(self, painter: QPainter, x: int, y: int, size: int):
        """Draw a simple settings gear icon."""
        painter.setPen(QPen(Qt.GlobalColor.darkGray, 2))
        painter.setBrush(QBrush(Qt.GlobalColor.gray))
        
        # Draw octagon (simplified gear)
        center_x, center_y = x + size // 2, y + size // 2
        radius = size // 3
        
        # Outer octagon
        painter.drawEllipse(center_x - radius, center_y - radius, radius * 2, radius * 2)
        
        # Inner circle
        inner_radius = radius // 2
        painter.setBrush(QBrush(Qt.GlobalColor.white))
        painter.drawEllipse(center_x - inner_radius, center_y - inner_radius, 
                          inner_radius * 2, inner_radius * 2)
    
    def _draw_calibration_icon(self, painter: QPainter, x: int, y: int, size: int):
        """Draw a simple crosshair icon for calibration."""
        painter.setPen(QPen(Qt.GlobalColor.darkGray, 2))
        
        center_x, center_y = x + size // 2, y + size // 2
        
        # Draw crosshair
        painter.drawLine(x + 5, center_y, x + size - 5, center_y)  # Horizontal
        painter.drawLine(center_x, y + 5, center_x, y + size - 5)  # Vertical
        
        # Draw target circles
        painter.setBrush(QBrush())  # No fill
        painter.drawEllipse(center_x - 15, center_y - 15, 30, 30)
        painter.drawEllipse(center_x - 8, center_y - 8, 16, 16)
    
    def _draw_generic_icon(self, painter: QPainter, x: int, y: int, size: int):
        """Draw a generic placeholder icon."""
        painter.setPen(QPen(Qt.GlobalColor.darkGray, 2))
        painter.setBrush(QBrush(Qt.GlobalColor.lightGray))
        painter.drawRect(x + 10, y + 10, size - 20, size - 20)


class DeviceListWidget(QWidget):
    """Placeholder device list widget with settings icon."""

    def __init__(self):
        super().__init__()
        layout = QVBoxLayout()
        self._icon_label = GenericIconLabel("Device list - GUI under development", "settings")
        layout.addWidget(self._icon_label)
        self.setLayout(layout)

    def update_devices(self, devices):
        """Update device list - placeholder implementation."""
        pass


class SessionControlWidget(QWidget):
    """Placeholder session control widget with settings icon."""

    def __init__(self):
        super().__init__()
        layout = QVBoxLayout()
        self._icon_label = GenericIconLabel("Session control - GUI under development", "settings")
        layout.addWidget(self._icon_label)
        self.setLayout(layout)


class StatusDisplayWidget(QWidget):
    """Placeholder status display widget with settings icon."""

    def __init__(self):
        super().__init__()
        layout = QVBoxLayout()
        self._icon_label = GenericIconLabel("Status display - GUI under development", "settings")
        layout.addWidget(self._icon_label)
        self.setLayout(layout)


class BluetoothControlWidget(QWidget):
    """Placeholder Bluetooth control widget with settings icon."""

    def __init__(self):
        super().__init__()
        layout = QVBoxLayout()
        self._icon_label = GenericIconLabel("Bluetooth control - GUI under development", "settings")
        layout.addWidget(self._icon_label)
        self.setLayout(layout)


class WiFiControlWidget(QWidget):
    """Placeholder WiFi control widget with settings icon."""

    def __init__(self):
        super().__init__()
        layout = QVBoxLayout()
        self._icon_label = GenericIconLabel("WiFi control - GUI under development", "settings")
        layout.addWidget(self._icon_label)
        self.setLayout(layout)


class SystemIntegrationWidget(QWidget):
    """Placeholder system integration widget with settings icon."""

    elevation_requested = pyqtSignal(str)

    def __init__(self):
        super().__init__()
        layout = QVBoxLayout()
        self._icon_label = GenericIconLabel("System integration - GUI under development", "settings")
        layout.addWidget(self._icon_label)
        self.setLayout(layout)


class CalibrationUtilityWidget(QWidget):
    """Calibration utility widget with crosshair icon."""
    
    def __init__(self):
        super().__init__()
        layout = QVBoxLayout()
        self._icon_label = GenericIconLabel("Camera calibration utilities", "calibration")
        layout.addWidget(self._icon_label)
        self.setLayout(layout)
