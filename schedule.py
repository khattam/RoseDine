# schedule.py

import scraper
from datetime import datetime, timedelta

def scrape_weekly_meals(file):
    start_date = datetime.now()
    week_dates = [start_date + timedelta(days=i) for i in range(6)]  

    for date in week_dates:
        day_url = f"https://rose-hulman.cafebonappetit.com/cafe/cafe/{date.strftime('%Y-%m-%d')}/"
        weekday = date.weekday()
        
        if weekday < 5:  
            meals = ['Breakfast', 'Lunch', 'Dinner']
        elif weekday == 5:  
            meals = ['Brunch']
        else:  
            meals = ['Brunch', 'Dinner']
        
        for meal in meals:
            file.write(f"Scraping {meal} for {date.strftime('%Y-%m-%d')}\n")
            scraper.get_meal(meal, day_url, file)

if __name__ == "__main__":
    with open('output.txt', 'a') as file:
        scrape_weekly_meals(file)
