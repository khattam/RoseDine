import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'breakfast_screen.dart';
import 'brunch_screen.dart';
import 'lunch_screen.dart';
import 'dinner_screen.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
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
      home: ScheduleScreen(),
    );
  }
}

class ScheduleScreen extends StatefulWidget {
  @override
  _ScheduleScreenState createState() => _ScheduleScreenState();
}

class _ScheduleScreenState extends State<ScheduleScreen> {
  int _selectedIndex = 0;
  DateTime selectedDate = DateTime.now();

  List<Widget> get _widgetOptions => _getMealScreens();
  List<BottomNavigationBarItem> get _navBarItems => _getNavBarItems();

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  void changeDay(int days) {
    setState(() {
      selectedDate = selectedDate.add(Duration(days: days));
      _selectedIndex = 0;
    });
  }

  List<Widget> _getMealScreens() {
    bool isWeekend = selectedDate.weekday == DateTime.saturday ||
        selectedDate.weekday == DateTime.sunday;

    return [
      if (isWeekend) BrunchScreen(),
      if (!isWeekend) BreakfastScreen(),
      if (!isWeekend) LunchScreen(),
      DinnerScreen(),
    ];
  }

  String getMealTime() {
    bool isWeekend = selectedDate.weekday == DateTime.saturday ||
        selectedDate.weekday == DateTime.sunday;
    switch (_navBarItems[_selectedIndex].label) {
      case 'Breakfast':
        return '7:00 AM - 10:00 AM';
      case 'Brunch':
        return '10:00 AM - 2:00 PM';
      case 'Lunch':
        return '11:00 AM - 2:00 PM';
      case 'Dinner':
        return isWeekend ? '5:00 PM - 7:00 PM' : '5:00 PM - 8:00 PM';
      default:
        return '';
    }
  }

  List<BottomNavigationBarItem> _getNavBarItems() {
    bool isWeekend = selectedDate.weekday == DateTime.saturday ||
        selectedDate.weekday == DateTime.sunday;

    return [
      if (isWeekend)
        BottomNavigationBarItem(
            icon: Icon(Icons.free_breakfast), label: 'Brunch'),
      if (!isWeekend)
        BottomNavigationBarItem(
            icon: Icon(Icons.free_breakfast), label: 'Breakfast'),
      if (!isWeekend)
        BottomNavigationBarItem(icon: Icon(Icons.lunch_dining), label: 'Lunch'),
      BottomNavigationBarItem(icon: Icon(Icons.dinner_dining), label: 'Dinner'),
    ];
  }

  Color getMealStatusColor() {
    final now = DateTime.now();
    final currentHour = now.hour + now.minute / 60.0;
    bool isWeekend = selectedDate.weekday == DateTime.saturday ||
        selectedDate.weekday == DateTime.sunday;
    bool isSelectedDateToday = selectedDate.year == now.year &&
        selectedDate.month == now.month &&
        selectedDate.day == now.day;  // Check if the selected date is today
    bool isOpen = false;

    if (!isSelectedDateToday) {
        return Colors.red;  // If it's not today, always return red
    }

    switch (_navBarItems[_selectedIndex].label) {
        case 'Breakfast':
            isOpen = !isWeekend && currentHour >= 7 && currentHour < 10;
            break;
        case 'Brunch':
            isOpen = isWeekend && currentHour >= 10 && currentHour < 14;
            break;
        case 'Lunch':
            isOpen = !isWeekend && currentHour >= 11 && currentHour < 14;
            break;
        case 'Dinner':
            isOpen = currentHour >= 17 && currentHour < (isWeekend ? 19 : 20);
            break;
    }

    return isOpen ? Colors.green : Colors.red;
}

  @override
  Widget build(BuildContext context) {
    String dateStr = DateFormat('EEE, dd MMM').format(selectedDate);
    String mealTime = getMealTime();

    return Scaffold(
      appBar: AppBar(
        title: Text('$dateStr, $mealTime'),
        actions: <Widget>[
          IconButton(
              icon: Icon(Icons.arrow_back_ios),
              onPressed: selectedDate.isAfter(DateTime.now())
                  ? () => changeDay(-1)
                  : null),
          IconButton(
              icon: Icon(Icons.arrow_forward_ios),
              onPressed: selectedDate.difference(DateTime.now()).inDays < 5
                  ? () => changeDay(1)
                  : null),
          Container(
            width: 20,
            height: 20,
            decoration: BoxDecoration(
              color: getMealStatusColor(),
              shape: BoxShape.circle,
            ),
            margin: EdgeInsets.all(10),
          ),
        ],
      ),
      body: Center(child: _widgetOptions.elementAt(_selectedIndex)),
      bottomNavigationBar: BottomNavigationBar(
        items: _navBarItems,
        currentIndex: _selectedIndex,
        selectedItemColor: Theme.of(context).primaryColor,
        onTap: _onItemTapped,
        type: BottomNavigationBarType.fixed,
      ),
    );
  }
}
