import subprocess
import schedule
from selenium import webdriver
from selenium.webdriver.chrome.service import Service

def run_schedule():
    chromedriver_path = 'PyScraping\\chromedriver-win64\\chromedriver.exe'
    service = Service(executable_path=chromedriver_path)
    driver = webdriver.Chrome(service=service)

    with open('PyScraping\\output.txt', 'a') as file:
        schedule.scrape_weekly_meals(driver, file)

    driver.quit()

def run_api_jsonify():
    subprocess.run(["python", "PyScraping\\api_jsonify.py"])

if __name__ == "__main__":

    run_schedule()


    print("schedule.py finished. Running api_jsonify.py...")


    run_api_jsonify()

    print("All tasks completed.")