  import 'package:flutter/material.dart';
  import 'package:intl/intl.dart';
  import 'Auth.dart';
  import 'ScheduleScreen.dart';

  void main() => runApp(const MyApp());

  class MyApp extends StatelessWidget {
    const MyApp({super.key});

    @override
    Widget build(BuildContext context) {
      return MaterialApp(
        title: 'Meal Schedule',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          useMaterial3: true,
          primarySwatch: Colors.blue,
          scaffoldBackgroundColor: Colors.grey.shade200,
        ),
        home: const AuthScreen(),  // Start with AuthScreen
      );
    }
  }
