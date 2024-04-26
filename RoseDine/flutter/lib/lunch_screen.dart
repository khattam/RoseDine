import 'package:flutter/material.dart';
import 'menu_item_screen.dart';

class LunchScreen extends StatelessWidget {
  final DateTime selectedDate;

  const LunchScreen({Key? key, required this.selectedDate}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MenuItemScreen(selectedDate: selectedDate, mealType: 'Lunch');
  }
}