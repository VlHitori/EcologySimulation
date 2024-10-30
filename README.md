# Ecosystem Simulation
## Буракевич Владислав

## Описание 
Данная программа симулирует экосистему с различными типами организмов: травоядными, плотоядными и растениями. С помощью настроек среды, таких как влажность и температура, программа управляет ростом и снижением популяций, учитывая потребности каждого организма.

Экосистема функционирует оптимально при наличии значительных запасов еды, воды и разнообразных организмов. Потребности в этих ресурсах оцениваются по шкале от 0 до 5. Также экосистема старается соблюдает закон, согласно которому на следующий трофический уровень передаётся не более 10% энергии, что важно для поддержания стабильности и баланса в природных взаимодействиях.

## Установка

Для работы с проектом необходимо установить [Maven](https://maven.apache.org/) и [Java](https://www.java.com/).

**Требования**

## Шаги по установке:

**Клонируйте репозиторий**:
   Откройте терминал и выполните следующую команду:
  
   ```bash
   git clone git@github.com:VlHitori/Simulation.git
   cd Simulation 
   ```
## Запуск
**Используйте скрипт** 

    ./run.sh
    
**Или команды:**

    mvn clean install
    java -cp target/*.jar com.cabachok.App

## Взаимодействие с Главным Меню

### В начале запуска программы вы попадаете в главное меню, где доступны четыре опции:

- **Менеджер экосистем:** Позволяет просматривать имеющиеся экосистемы, создавать новые, редактировать, удалять и сохранять их.
- **Посмотреть информацию о выбранной экосистеме:** Позволяет получить подробную информацию о выбранной экосистеме.
- **Запустить симуляцию экосистемы:** Позволяет вам влиять на экосистему с помощью доступных команд.
- **Выход:** Завершает работу программы.

### Доступные команды для симуляции экосистемы:

- **step:** Выполняет один шаг симуляции.
- **startAuto:** Запускает автоматическую симуляцию.
- **predict:** Делает предсказания о будущем состоянии экосистемы.
- **info:** Отображает информацию о текущем состоянии экосистемы.
- **change:** Позволяет изменять параметры экосистемы.
- **exit:** Выход из симуляции.    


## Алгоритм Симуляции Экосистемы
Симуляция экосистемы реализована на основе модели взаимодействия организмов и условий окружающей среды. Алгоритм разбит на несколько ключевых этапов, каждый из которых описывает влияние параметров экосистемы на состояние и популяцию каждого организма:

**1. Инициализация Экосистемы**     
Экосистема создается с заданными параметрами, включая типы организмов (травоядные, плотоядные, растения), а также начальные условия среды, такие как температура и влажность. Каждый организм имеет свои требования к еде, воде, температуре и влажности, которые будут использоваться в дальнейшем для расчета состояния популяции.

**2. Расчёт влияния ресурсов воды и еды**   
Для расчёта влияния ресурсов воды и еды у нас есть общее количество ресурсов (воды) и потребность организма в этих ресурсах (для еды индивидуальная потребность на каждый организм).    
— Введём константу изобилия, которая определяет, насколько больше доступного ресурса по отношению к потребности, а также регулирует часть ресурса, доступную за раз. Когда ресурсы покрывают потребности, устанавливается максимальный коэффициент роста, который дополнительно сглаживается через весовой фактор (аналогично факторам температуры и влажности).    
— Если ресурса недостаточно, определяем количество на организм и рассчитываем, в какой мере потребности покрываются. Отношение доступного к требуемому ресурсу подставляется в формулу resourceImpact для расчёта коэффициента влияния. Полученное значение корректируется весом влияния и становится итоговым коэффициентом ресурса (еды или воды) для роста.

**3. Расчёт влияния влажности и температуры**   
В симуляции влияние факторов влажности и температуры на популяцию рассчитывается следующим образом: сначала вычисляется разница между фактическими и оптимальными значениями влажности и температуры. Затем на основе этой разницы, с помощью экспоненциальных формул, определяются коэффициенты влияния для каждого фактора (humidityImpact и temperatureImpact). Эти коэффициенты показывают, насколько отклонение от оптимальных условий воздействует на популяцию. Далее, полученные значения корректируются с помощью констант HumidityInfluenceFactor и TemperatureInfluenceFactor, чтобы сделать влияние более умеренным, приближая его к единице в зависимости от величины констант.

**4. Потребление пищи и обновление популяций**  
В процессе потребления пищи в симуляции осуществляется следующий алгоритм: на предыдущем этапе мы определили, сколько ресурсов доступно для потребления, и это количество фиксируется для каждого типа организмов. Затем от общего количества доступной пищи отнимается равное количество от всех организмов одного типа, что обеспечивает пропорциональное распределение ресурсов среди всех членов популяции данного типа и позволяет сохранить баланс в экосистеме. После распределения пищи активируется коэффициент роста-смерти, который отвечает за обновление количества организмов в популяции. Этот коэффициент учитывает, сколько организмов выжило и сколько из них погибло в результате нехватки ресурсов. В зависимости от полученного значения общее количество организмов в популяции корректируется, что отражает изменение в численности из-за процессов потребления пищи и выживания.