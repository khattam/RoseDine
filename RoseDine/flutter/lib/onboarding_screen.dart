import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:rosedine/auth.dart';
import 'package:rosedine/widgets/custom_text_widget.dart';
import 'dart:convert';


class OnboardingScreen extends StatefulWidget {
  @override
  _OnboardingScreenState createState() => _OnboardingScreenState();
}

class _OnboardingScreenState extends State<OnboardingScreen> with SingleTickerProviderStateMixin {
  final _formKey = GlobalKey<FormState>();
  final _fnameController = TextEditingController();
  final _lnameController = TextEditingController();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();

  late AnimationController _animationController;
  late Animation<double> _fadeAnimation;
  late Animation<double> _scaleAnimation;

  final String backendUrl = 'http://localhost:8081/api/users';

  @override
  void initState() {
    super.initState();
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
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  Future<void> _register() async {
    final fname = _fnameController.text;
    final lname = _lnameController.text;
    final email = _emailController.text;
    final password = _passwordController.text;

    final url = Uri.parse('$backendUrl/register');
    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: json.encode({'fname': fname, 'lname': lname, 'email': email, 'password': password}),
    );

    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Registration successful')));
      Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => AuthScreen()));
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Registration failed: ${response.body}')));
    }
  }

  String? _emailValidator(String? email) {
    if (email == null || !email.contains('@')) {
      return 'Please enter a valid email address';
    }
    if (!email.endsWith('@rose-hulman.edu')) {
      return 'Email must end with @rose-hulman.edu';
    }
    return null;
  }

  String? _passwordValidator(String? password) {
    if (password == null || password.length < 6) {
      return 'Password must be at least 6 characters long';
    }
    return null;
  }




  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.blueGrey[900],
      appBar: AppBar(title: const Text('Register'), backgroundColor: Colors.blueGrey[900]),
      body: SafeArea(
        child: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.all(24.0),
            child: Form(
              key: _formKey,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  FadeTransition(
                    opacity: _fadeAnimation,
                    child: ScaleTransition(
                      scale: _scaleAnimation,
                      child: Container(
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
                            child: Image.asset(
                              'assets/RoseDine.jpg',
                              fit: BoxFit.cover,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 40),
                  CustomTextField(
                    controller: _fnameController,
                    labelText: 'First Name',
                  ),
                  const SizedBox(height: 20),
                  CustomTextField(
                    controller: _lnameController,
                    labelText: 'Last Name',
                  ),
                  const SizedBox(height: 20),
                  CustomTextField(
                    controller: _emailController,
                    labelText: 'Email',
                    keyboardType: TextInputType.emailAddress,
                    validator: _emailValidator,
                  ),
                  const SizedBox(height: 20),
                  CustomTextField(
                    controller: _passwordController,
                    labelText: 'Password',
                    obscureText: true,
                    validator: _passwordValidator,
                  ),
                  const SizedBox(height: 30),
                  OutlinedButton(
                    onPressed: () {
                      if (_formKey.currentState!.validate()) {
                        _register();
                      }
                    },
                    style: OutlinedButton.styleFrom(
                      side: BorderSide(color: Colors.white),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(25),
                      ),
                      padding: const EdgeInsets.symmetric(horizontal: 40, vertical: 12),
                    ),
                    child: const Text(
                      'Register',
                      style: TextStyle(fontSize: 18, color: Colors.white),
                    ),
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

