import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:rosedine/auth.dart';

void main() {
  runApp(
    ProviderScope(
      child: MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'RoseDine',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        primaryColor: const Color(0xFFAB8532),
        scaffoldBackgroundColor: Colors.blueGrey[300],
        appBarTheme: const AppBarTheme(
          backgroundColor: Colors.blueGrey,
          elevation: 0,
          iconTheme: IconThemeData(color: Colors.white),
        ),
        toggleButtonsTheme: ToggleButtonsThemeData(
          fillColor: const Color(0xFFAB8532),
          selectedBorderColor: const Color(0xFFAB8532),
          selectedColor: Colors.white,
        ),
      ),
      home: const AuthScreen(),
    );
  }
}