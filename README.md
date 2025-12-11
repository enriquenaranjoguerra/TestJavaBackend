Element structure
- Category
- Block (contained in category)
- Theme (contained in block)
- Question (contained in theme)
- Answer (contained in question)

Listening port: 8080
Localhost test URL: http://localhost:8080/

Compile in Windows
jpackage --input target --name "TestAppPortable" --main-jar TestApp-1.0.0.jar --type app-image --icon test.ico --dest portable_output
