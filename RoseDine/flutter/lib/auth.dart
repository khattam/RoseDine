import 'dart:math';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:rosedine/widgets/custom_button_widget.dart';
import 'package:rosedine/widgets/custom_text_widget.dart' as custom_widget;
import 'package:shared_preferences/shared_preferences.dart';
import 'schedule_screen.dart';
import 'onboarding_screen.dart';

class AuthScreen extends StatefulWidget {
  const AuthScreen({super.key});

  @override
  _AuthScreenState createState() => _AuthScreenState();
}

class _AuthScreenState extends State<AuthScreen> with SingleTickerProviderStateMixin {
  final _formKey = GlobalKey<FormState>();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final int ipSwitcher = 1;

  late String backendUrl;

  late AnimationController _animationController;
  late Animation<double> _fadeAnimation;
  late Animation<double> _scaleAnimation;

  @override
  void initState() {
    super.initState();
    _checkLoginStatus();
    _animationController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 800),
    );
    _fadeAnimation = Tween<double>(begin: 0.0, end: 1.0).animate(
      CurvedAnimation(parent: _animationController, curve: Curves.easeIn),
    );
    _scaleAnimation = Tween<double>(begin: 0.8, end: 1.0).animate(
      CurvedAnimation(parent: _animationController, curve: Curves.easeOut),
    );
    _animationController.forward();

    // Set the backendUrl based on the ipSwitcher value
    backendUrl = ipSwitcher == 0 ? 'http://localhost:8081' : 'http://137.112.225.85:8081';
  }

  Future<void> _checkLoginStatus() async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getString('userId');
    if (userId != null) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => ScheduleScreen()),
      );
    }
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  Future<void> _submitForm() async {
    final email = _emailController.text;
    final password = _passwordController.text;

    final url = Uri.parse('$backendUrl/api/users/login');
    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: json.encode({'email': email, 'password': password}),
    );

    if (response.statusCode == 200) {
      final userId = response.body;
      await _saveUserId(userId);
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Login successful')));
      Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => ScheduleScreen()));
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Failed to log in: ${response.body}')));
    }
  }

  Future<void> _saveUserId(String userId) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('userId', userId);
  }

  final List<String> welcomeMessages = [
    'Welcome to RoseDine!',
    'A better bon appétit experience',
    'Drinking in the eyecandy :)',
    'Feeding your foodie soul, one scroll at a time!',
    'Inspired By Minecraft !',
    'To eat away the pain of exams...',
    'Food so good, you\'ll forget about your student loans!',
    'Procrastination never tasted so delicious!',
    'Is it just us, or does food taste better when you\'re not paying?',
    'Putting the "pro" in procrastination...',
    'Food, glorious food!',
    'by Agnay and Medhansh',
    'We\'re not saying we\'re the best, but we\'re not not saying it either',
  ];

  @override
  Widget build(BuildContext context) {
    final random = Random();
    final selectedMessage = welcomeMessages[random.nextInt(welcomeMessages.length)];

    return Scaffold(
      backgroundColor: Colors.blueGrey[900],
      body: SafeArea(
        child: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.all(24.0),
            child: Form(
              key: _formKey,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const SizedBox(height: 30),
                  FadeTransition(
                    opacity: _fadeAnimation,
                    child: ScaleTransition(
                      scale: _scaleAnimation,
                      child: Stack(
                        alignment: Alignment.center,
                        children: [
                          Container(
                            width: 330,
                            height: 330,
                            decoration: BoxDecoration(
                              shape: BoxShape.circle,
                              border: Border.all(
                                color: Color(0xFFAB8532),
                                width: 2,
                              ),
                            ),
                            child: ClipOval(
                              child: ShaderMask(
                                shaderCallback: (Rect bounds) {
                                  return RadialGradient(
                                    center: Alignment.center,
                                    radius: 0.8,
                                    colors: [Colors.white, Colors.white.withOpacity(0.0)],
                                  ).createShader(bounds);
                                },
                                blendMode: BlendMode.dstIn,
                                child: Transform.scale(
                                  scale: 1,
                                  child: Image.asset(
                                    'assets/RoseDine.jpg',
                                    fit: BoxFit.cover,
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(height: 40),
                  Center(
                    child: Text(
                      selectedMessage,
                      style: const TextStyle(
                        fontSize: 24,
                        fontWeight: FontWeight.bold,
                        color: Colors.white,
                      ),
                      textAlign: TextAlign.center,
                    ),
                  ),
                  const SizedBox(height: 30),
                  custom_widget.CustomTextField(
                    controller: _emailController,
                    labelText: 'Email',
                    keyboardType: TextInputType.emailAddress,
                    obscureText: false,
                  ),
                  const SizedBox(height: 30),
                  custom_widget.CustomTextField(
                    controller: _passwordController,
                    labelText: 'Password',
                    obscureText: true, // Ensures the text is obscured for password input
                  ),
                  const SizedBox(height: 30),
                  MyButton(
                    onTap: () {
                      if (_formKey.currentState!.validate()) {
                        _submitForm();
                      }
                    },
                    text: 'Login',
                  ),
                  const SizedBox(height: 20),
                  MyButton(
                    onTap: () {
                      Navigator.push(context, MaterialPageRoute(builder: (context) => OnboardingScreen()));
                    },
                    text: 'Create an Account',
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
