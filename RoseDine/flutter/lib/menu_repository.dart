import 'dart:convert';
import 'package:flutter/services.dart' show rootBundle;

class MenuRepository {
  Future<Map<String, dynamic>> loadMenuData() async {
    final jsonString = await rootBundle.loadString('assets/menu.json');
    final jsonResponse = json.decode(jsonString);
    return jsonResponse;
  }
}
