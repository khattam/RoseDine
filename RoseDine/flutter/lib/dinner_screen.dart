import 'package:flutter/material.dart';
import 'menu_item_screen.dart';

class DinnerScreen extends StatelessWidget {
  final DateTime selectedDate;

  const DinnerScreen({Key? key, required this.selectedDate}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MenuItemScreen(selectedDate: selectedDate, mealType: 'Dinner');
  }
}