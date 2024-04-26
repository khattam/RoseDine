import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'ScheduleScreen.dart';  // Next screen after authentication

class AuthScreen extends StatefulWidget {
  const AuthScreen({super.key});

  @override
  _AuthScreenState createState() => _AuthScreenState();
}

class _AuthScreenState extends State<AuthScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  // Backend URL
  final String backendUrl = 'http://localhost:8081/api/users';

  // Function to register a user
  Future<void> registerUser(String email, String password) async {
    final response = await http.post(
      Uri.parse('http://localhost:8081/api/users/register'),
      headers: {
        'Content-Type': 'application/json',
      },
      body: json.encode({
        'email': email,
        'password': password,
      }),
    );

    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Registration successful')),
      );
      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => ScheduleScreen()),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Failed to register: ${response.body}')),
      );
    }
  }

  // Function to validate email and password
  String? _emailValidator(String? email) {
    if (email == null || !EmailValidator.validate(email)) {
      return 'Please enter a valid email address';
    }

    if (!email.endsWith('@rose-hulman.edu')) {
      return 'Invalid domain, use a @rose-hulman.edu email';
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
      appBar: AppBar(title: const Text('Authentication')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Text(
                'Welcome to RoseDine',
                style: Theme.of(context).textTheme.headline5,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 40),
              TextFormField(
                controller: _emailController,
                decoration: const InputDecoration(
                  labelText: 'Email',
                  border: OutlineInputBorder(),
                ),
                validator: _emailValidator,
                keyboardType: TextInputType.emailAddress,
              ),
              const SizedBox(height: 20),
              TextFormField(
                controller: _passwordController,
                decoration: const InputDecoration(
                  labelText: 'Password',
                  border: OutlineInputBorder(),
                ),
                validator: _passwordValidator,
                obscureText: true,
              ),
              const SizedBox(height: 30),
              ElevatedButton(
                onPressed: () {
                  if (_formKey.currentState!.validate()) {
                    final email = _emailController.text;
                    final password = _passwordController.text;
                    registerUser(email, password);
                  }
                },
                child: const Text('Register'),
              ),
              ElevatedButton(
                onPressed: _bypass,
                child: const Text('Bypass'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  // Function to bypass authentication
  void _bypass() {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => ScheduleScreen()),
    );
  }
}
