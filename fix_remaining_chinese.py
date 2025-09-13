#!/usr/bin/env python3
"""
Comprehensive Chinese comment translation for remaining source files.
Excludes migration backup directories and generated files.
"""

import os
import re
import glob
from pathlib import Path

# Enhanced thermal imaging translation dictionary
THERMAL_TRANSLATIONS = {
    # UI and Menu Terms
    "菜单": "menu",
    "设置": "settings", 
    "选择": "selection",
    "选中": "selected",
    "切换": "switch",
    "点击": "click",
    "按钮": "button",
    "状态": "state",
    "模式": "mode",
    "类型": "type",
    "配置": "configuration",
    "参数": "parameter",
    "数据": "data",
    "文件": "file",
    "路径": "path",
    "保存": "save",
    "加载": "load",
    "删除": "delete",
    "添加": "add",
    "修改": "modify",
    "更新": "update",
    "刷新": "refresh",
    "重置": "reset",
    "清空": "clear",
    "复制": "copy",
    "粘贴": "paste",
    "导入": "import",
    "导出": "export",
    
    # Thermal Imaging Terms
    "红外": "infrared",
    "热成像": "thermal imaging",
    "温度": "temperature", 
    "测温": "temperature measurement",
    "伪彩": "pseudo color",
    "调色板": "palette",
    "色温": "color temperature",
    "增益": "gain",
    "对比度": "contrast",
    "亮度": "brightness",
    "饱和度": "saturation",
    "校准": "calibration",
    "标定": "calibration",
    "配准": "registration",
    "融合": "fusion",
    "可见光": "visible light",
    "双光": "dual light",
    "白热": "white hot",
    "黑热": "black hot",
    "铁红": "iron red",
    "彩虹": "rainbow",
    "灰度": "grayscale",
    "范围": "range",
    "最高": "maximum",
    "最低": "minimum",
    "平均": "average",
    "中心": "center",
    "点": "point",
    "线": "line",
    "面": "area",
    "区域": "region",
    "框选": "selection box",
    "测量": "measurement",
    "分析": "analysis",
    "报告": "report",
    "图像": "image",
    "图片": "image",
    "照片": "photo",
    "视频": "video",
    "录制": "recording",
    "播放": "playback",
    "暂停": "pause",
    "停止": "stop",
    "开始": "start",
    "结束": "end",
    "完成": "complete",
    "成功": "success",
    "失败": "failed",
    "错误": "error",
    "异常": "exception",
    "警告": "warning",
    "提示": "tip",
    "信息": "info",
    "消息": "message",
    "通知": "notification",
    
    # Hardware Terms
    "设备": "device",
    "相机": "camera",
    "镜头": "lens", 
    "传感器": "sensor",
    "机芯": "core",
    "模组": "module",
    "组件": "component",
    "硬件": "hardware",
    "软件": "software",
    "固件": "firmware",
    "版本": "version",
    "型号": "model",
    "序列号": "serial number",
    "连接": "connection",
    "断开": "disconnect",
    "通信": "communication",
    "协议": "protocol",
    "接口": "interface",
    "端口": "port",
    "网络": "network",
    "蓝牙": "bluetooth",
    "WiFi": "WiFi",
    "USB": "USB",
    
    # Technical Terms  
    "算法": "algorithm",
    "处理": "processing",
    "计算": "calculation",
    "转换": "conversion",
    "解析": "parsing",
    "编码": "encoding",
    "解码": "decoding",
    "压缩": "compression",
    "解压": "decompression",
    "格式": "format",
    "协议": "protocol",
    "标准": "standard",
    "规范": "specification",
    "文档": "document",
    "说明": "description",
    "注释": "comment",
    "备注": "remark",
    "标记": "marker",
    "标签": "tag",
    "标题": "title",
    "名称": "name",
    "属性": "property",
    "方法": "method",
    "函数": "function",
    "变量": "variable",
    "常量": "constant",
    "枚举": "enum",
    "类": "class",
    "接口": "interface",
    "继承": "inheritance",
    "实现": "implementation",
    "重写": "override",
    "重载": "overload",
    "构造": "constructor",
    "析构": "destructor",
    "初始化": "initialization",
    "销毁": "destroy",
    "创建": "create",
    "释放": "release",
    "分配": "allocate",
    "回收": "recycle",
    "缓存": "cache",
    "缓冲": "buffer",
    "队列": "queue",
    "堆栈": "stack",
    "线程": "thread",
    "进程": "process",
    "任务": "task",
    "作业": "job",
    "服务": "service",
    "客户端": "client",
    "服务器": "server",
    "数据库": "database",
    "存储": "storage",
    "内存": "memory",
    "磁盘": "disk",
    "文字": "text",
    "水印": "watermark",
    
    # Complex phrases patterns
    "暂时通过此全局变量，区分不同的模组:指令调用，业务processing": "Temporarily use this global variable to distinguish different modules: command invocation, business processing",
    "水印文字": "watermark text",
    "文字内容": "text content",
    "显示文字": "display text",
    "文字大小": "text size",
    "文字颜色": "text color",
    "文字样式": "text style",
    "文字位置": "text position",
    "文字对齐": "text alignment",
}

def translate_chinese_text(text):
    """Translate Chinese text to English using thermal imaging dictionary."""
    # First try exact phrase matches
    for chinese, english in THERMAL_TRANSLATIONS.items():
        if chinese in text:
            text = text.replace(chinese, english)
    
    return text

def process_file(filepath):
    """Process a single file to translate Chinese comments."""
    try:
        with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()
        
        # Check if file contains Chinese characters
        if not re.search(r'[\u4e00-\u9fff]', content):
            return False
        
        original_content = content
        lines = content.split('\n')
        modified = False
        
        for i, line in enumerate(lines):
            if re.search(r'[\u4e00-\u9fff]', line):
                # Translate the line
                translated_line = translate_chinese_text(line)
                if translated_line != line:
                    lines[i] = translated_line
                    modified = True
                    print(f"  Line {i+1}: {line.strip()}")
                    print(f"    -> {translated_line.strip()}")
        
        if modified:
            new_content = '\n'.join(lines)
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(new_content)
            return True
        
    except Exception as e:
        print(f"Error processing {filepath}: {e}")
        return False
    
    return False

def main():
    """Main function to process all source files."""
    print("=== Comprehensive Chinese Comment Translation ===")
    
    # Define patterns for source files
    source_patterns = [
        "**/*.kt",
        "**/*.java",
        "**/*.xml"
    ]
    
    # Exclude patterns
    exclude_patterns = [
        "**/migration_backup_*/**",
        "**/build/**",
        "**/.*/**",
        "**/.git/**"
    ]
    
    total_files = 0
    modified_files = 0
    
    for pattern in source_patterns:
        files = glob.glob(pattern, recursive=True)
        
        for filepath in files:
            # Skip excluded paths
            skip_file = False
            for exclude in exclude_patterns:
                if any(part.startswith(exclude.replace('**/','').replace('/**','')) for part in Path(filepath).parts):
                    skip_file = True
                    break
            
            if skip_file:
                continue
                
            total_files += 1
            
            if process_file(filepath):
                modified_files += 1
                print(f"✓ Translated: {filepath}")
    
    print(f"\n=== Translation Complete ===")
    print(f"Files processed: {total_files}")
    print(f"Files modified: {modified_files}")

if __name__ == "__main__":
    main()