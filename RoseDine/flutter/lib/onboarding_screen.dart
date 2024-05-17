import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:rosedine/widgets/custom_text_widget.dart' as custom_widget;

class OnboardingScreen extends StatefulWidget {
  @override
  _OnboardingScreenState createState() => _OnboardingScreenState();
}

class _OnboardingScreenState extends State<OnboardingScreen> {
  final _formKey = GlobalKey<FormState>();
  final _fnameController = TextEditingController();
  final _lnameController = TextEditingController();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  String? _verificationToken;

  Future<void> _register() async {
    final fname = _fnameController.text;
    final lname = _lnameController.text;
    final email = _emailController.text;
    final password = _passwordController.text;

    final url = Uri.parse('http://localhost:8081/api/users/register');
    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'fname': fname,
        'lname': lname,
        'email': email,
        'password': password
      }),
    );
    if (response.statusCode == 200) {
      final responseBody = response.body;
      print('Response body: $responseBody');

      // Extract the JWT token from the response
      final tokenStartIndex = responseBody.indexOf('Token: ') +
          'Token: '.length;
      _verificationToken = responseBody.substring(tokenStartIndex);

      print('Verification token: $_verificationToken');
      _showVerificationCodeDialog(email);
    }
      else {
      print('Registration failed: ${response.body}');
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Registration failed: ${response.body}')));
    }
  }

  void _showVerificationCodeDialog(String email) {
    TextEditingController _codeController = TextEditingController();

    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          backgroundColor: Colors.blueGrey[900],
          title: Text(
            'Verify Email',
            style: TextStyle(color: Colors.white),
          ),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(
                'A verification code has been sent to $email.',
                style: TextStyle(color: Colors.white),
              ),
              SizedBox(height: 16),
              custom_widget.CustomTextField(
                controller: _codeController,
                labelText: 'Enter your verification code',
                obscureText: false,
              ),
            ],
          ),
          actions: <Widget>[
            TextButton(
              onPressed: () {
                String code = _codeController.text;
                _verifyEmail(code);
                Navigator.of(context).pop();
              },
              child: Text(
                'Verify',
                style: TextStyle(color: Colors.white),
              ),
            ),
          ],
        );
      },
    );
  }

  void _promptForVerificationCode(String email) {
    TextEditingController codeController = TextEditingController();

    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text('Verify Email'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text('A verification code has been sent to $email.'),
              TextField(
                controller: codeController,
                decoration: InputDecoration(hintText: 'Enter your verification code'),
              ),
            ],
          ),
          actions: <Widget>[
            TextButton(
              child: Text('Verify'),
              onPressed: () async {
                Navigator.of(context).pop();
                await _verifyEmail(codeController.text);
              },
            ),
          ],
        );
      },
    );
  }

  Future<void> _verifyEmail(String code) async {
    final url = Uri.parse('http://localhost:8081/api/users/verify-email');
    final fname = _fnameController.text;
    final lname = _lnameController.text;
    final email = _emailController.text;
    final password = _passwordController.text;
    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'token': _verificationToken,
        'code': code,
        'fname': fname,
        'lname': lname,
        'email': email,
        'password': password
      }),
    );

    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Email verified successfully')));
      // Navigate to the login screen or perform any other necessary actions
      Navigator.pushReplacementNamed(context, '/auth');
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Verification failed: ${response.body}')));
    }
  }
  @override
  Widget build(BuildContext context) {
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
                  custom_widget.CustomTextField(
                    controller: _fnameController,
                    labelText: 'First Name',
                  ),
                  const SizedBox(height: 20),
                  custom_widget.CustomTextField(
                    controller: _lnameController,
                    labelText: 'Last Name',
                  ),
                  const SizedBox(height: 20),
                  custom_widget.CustomTextField(
                    controller: _emailController,
                    labelText: 'Email',
                    keyboardType: TextInputType.emailAddress,
                  ),
                  const SizedBox(height: 20),
                  custom_widget.CustomTextField(
                    controller: _passwordController,
                    labelText: 'Password',
                    obscureText: true,
                  ),
                  const SizedBox(height: 30),
                  ElevatedButton(
                    onPressed: () {
                      if (_formKey.currentState!.validate()) {
                        _register();
                      }
                    },
                    child: Text('Register'),
                  ),
                  const SizedBox(height: 20),
                  ElevatedButton(
                    onPressed: () {
                      Navigator.pushReplacementNamed(context, '/auth');
                    },
                    child: Text('Login'),
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
