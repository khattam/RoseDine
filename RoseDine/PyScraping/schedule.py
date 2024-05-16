import scraper
from datetime import datetime, timedelta
from selenium import webdriver
from selenium.webdriver.chrome.service import Service

def scrape_weekly_meals(driver, file):
    start_date = datetime.now()
    week_dates = [start_date + timedelta(days=i) for i in range(7)]

    for date in week_dates:
        scrape_day_meals(date, driver, file)

def scrape_next_day_meal(driver, file):
    next_day = datetime.now() + timedelta(days=7)
    scrape_day_meals(next_day, driver, file)

def scrape_day_meals(date, driver, file):
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
        scraper.get_meal(meal, day_url, driver, file)

if __name__ == "__main__":
    chromedriver_path = 'PyScraping\\chromedriver-win64\\chromedriver.exe'
    #chromedriver_path = 'chromedriver-win64\\chromedriver.exe'
    service = Service(executable_path=chromedriver_path)
    driver = webdriver.Chrome(service=service)

    with open('PyScraping\\output.txt', 'a') as file:
        scrape_weekly_meals(driver, file)  
        # scrape_next_day_meal(driver, file)  # Uncomment to run for +7th day's meal

    driver.quit()
