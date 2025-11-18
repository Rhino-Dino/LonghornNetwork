import os
import re

# Directory containing your Java files
SRC_DIR = "./src"  # Change if needed
OUTPUT_FILE = "uml_diagram.txt"

# Regex patterns to extract classes, fields, and methods
CLASS_PATTERN = re.compile(r'public\s+(abstract\s+)?class\s+(\w+)(\s+extends\s+\w+)?(\s+implements\s+[\w, ]+)?\s*{')
FIELD_PATTERN = re.compile(r'(public|protected|private)\s+([\w<>\[\]]+)\s+(\w+);')
METHOD_PATTERN = re.compile(r'(public|protected|private)\s+([\w<>\[\]]+)\s+(\w+)\s*\(([^)]*)\)')

uml_lines = []

for root, _, files in os.walk(SRC_DIR):
    for file in files:
        if file.endswith(".java"):
            filepath = os.path.join(root, file)
            with open(filepath, "r") as f:
                content = f.read()

            # Find all classes
            for class_match in CLASS_PATTERN.finditer(content):
                class_name = class_match.group(2)
                class_extends = class_match.group(3) if class_match.group(3) else ""
                class_implements = class_match.group(4) if class_match.group(4) else ""
                uml_lines.append(f"Class: {class_name}{class_extends}{class_implements}")

                # Find fields
                for field_match in FIELD_PATTERN.finditer(content):
                    visibility, ftype, fname = field_match.groups()
                    uml_lines.append(f"  {visibility} {fname} : {ftype}")

                # Find methods
                for method_match in METHOD_PATTERN.finditer(content):
                    visibility, rtype, mname, params = method_match.groups()
                    uml_lines.append(f"  {visibility} {mname}({params}) : {rtype}")

                uml_lines.append("")  # Blank line between classes

# Write UML-like text to file
with open(OUTPUT_FILE, "w") as out:
    out.write("\n".join(uml_lines))

print(f"UML text file generated: {OUTPUT_FILE}")
