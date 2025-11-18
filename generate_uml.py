import os
import re

# Directory containing your Java files
SRC_DIR = "./src"  # Change if needed
OUTPUT_FILE = "uml_diagram.txt"

# Regex patterns to extract classes, fields, and methods
CLASS_PATTERN = re.compile(r'public\s+(abstract\s+)?class\s+(\w+)(\s+extends\s+(\w+))?(\s+implements\s+[\w, ]+)?\s*{')
FIELD_PATTERN = re.compile(r'(public|protected|private)\s+([\w<>\[\]]+)\s+(\w+);')
METHOD_PATTERN = re.compile(r'(public|protected|private)\s+([\w<>\[\]]+)\s+(\w+)\s*\(([^)]*)\)')

uml_lines = []

def make_box_line(text, width):
    """Helper to create a line with | padding."""
    return f"| {text.ljust(width)} |"

for root, _, files in os.walk(SRC_DIR):
    for file in files:
        if file.endswith(".java"):
            filepath = os.path.join(root, file)
            with open(filepath, "r") as f:
                content = f.read()

            # Find all classes
            for class_match in CLASS_PATTERN.finditer(content):
                class_name = class_match.group(2)
                parent_class = class_match.group(4)
                class_implements = class_match.group(5) if class_match.group(5) else ""

                # Gather content lines
                content_lines = [f"Class: {class_name}"]
                if parent_class:
                    content_lines.append(f"Extends --> {parent_class}")
                if class_implements:
                    content_lines.append(f"Implements {class_implements.strip()}")
                content_lines.append("Fields:")
                for field_match in FIELD_PATTERN.finditer(content):
                    visibility, ftype, fname = field_match.groups()
                    content_lines.append(f"  {visibility} {fname} : {ftype}")
                content_lines.append("Methods:")
                for method_match in METHOD_PATTERN.finditer(content):
                    visibility, rtype, mname, params = method_match.groups()
                    content_lines.append(f"  {visibility} {mname}({params}) : {rtype}")

                # Determine box width
                max_width = max(len(line) for line in content_lines) + 2  # padding

                # Top border
                uml_lines.append("-" * (max_width + 4))
                # Content lines
                for line in content_lines:
                    uml_lines.append(make_box_line(line, max_width))
                # Bottom border
                uml_lines.append("-" * (max_width + 4))
                uml_lines.append("")  # blank line between classes

# Write UML-like text to file
with open(OUTPUT_FILE, "w") as out:
    out.write("\n".join(uml_lines))

print(f"UML text file generated: {OUTPUT_FILE}")
