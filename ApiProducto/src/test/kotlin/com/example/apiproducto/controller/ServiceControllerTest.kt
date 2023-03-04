package com.example.apiproducto.controller

//@ExtendWith(MockKExtension::class)
//@SpringBootTest
//class ServiceControllerTest {
//    @MockK
//    private lateinit var service: ServicesService
//
//    @InjectMockKs
//    lateinit var controller: ServiceController
//
//    private val serviceTest = Service(
//        price = 2.5,
//        available = true,
//        description = "Descripción",
//        url = "",
//        category = ServiceCategory.AMPLIFIER_REPAIR,
//    )
//
//    init {
//        MockKAnnotations.init(this)
//    }
//
//    @Test
//    fun findAll() = runTest {
//        coEvery { service.findAllServices() } returns listOf(serviceTest)
//
//        val result = controller.findAll()
//        val res = result.body!!
//
//        assertAll(
//            { assertEquals(result.statusCode, HttpStatus.OK) },
//            { assertEquals(res, listOf(serviceTest)) }
//        )
//        coVerify { service.findAllServices() }
//    }
//
//    // TODO faltarian los test de validacion que aun no esta implementada
//    @Test
//    fun create() = runTest {
//        coEvery { service.saveService(any()) } returns serviceTest
//
//        val result = controller.create(
//            ServiceCreateDto(
//                serviceTest.price,
//                serviceTest.available,
//                serviceTest.description,
//                serviceTest.url,
//                serviceTest.category.name
//            )
//        )
//        val res = result.body!!
//
//        assertAll(
//            { assertEquals(result.statusCode, HttpStatus.CREATED) },
//            { assertEquals(res.price, serviceTest.price) },
//            { assertEquals(serviceTest.category, res.category) },
//            { assertEquals(serviceTest.url, res.url) },
//            { assertEquals(serviceTest.available, res.available) }
//        )
//        coVerify { service.saveService(any()) }
//    }
//
//    @Test
//    fun findById() = runTest {
//        coEvery { service.findServiceById(any()) } returns serviceTest
//
//        val result = controller.findById(1)
//        val res = result.body!!
//
//        assertAll(
//            { assertEquals(result.statusCode, HttpStatus.OK) },
//            { assertEquals(res.price, serviceTest.price) },
//            { assertEquals(serviceTest.category, res.category) },
//            { assertEquals(serviceTest.url, res.url) },
//            { assertEquals(serviceTest.available, res.available) }
//        )
//        coVerify { service.findServiceById(any()) }
//    }
//
//    @Test
//    fun findByIdNotFound() = runTest {
//        coEvery { service.findServiceById(any()) } returns null
//
//        val result = controller.findById(-1)
//
//        assertAll(
//            { assertEquals(result.statusCode, HttpStatus.NOT_FOUND) },
//        )
//        coVerify { service.findServiceById(any()) }
//    }
//
//    @Test
//    fun delete() = runTest {
//        coEvery { service.deleteService(any()) } returns true
//
//        val result = controller.delete(1)
//
//        assertAll(
//            { assertEquals(result.statusCode, HttpStatus.NO_CONTENT) },
//        )
//        coVerify { service.deleteService(any()) }
//    }
//
//    @Test
//    fun deleteNotFound() = runTest {
//        coEvery { service.deleteService(any()) } throws ServiceNotFoundException("No existe el servicio con id")
//
//        val result = assertThrows<ResponseStatusException> {
//            controller.delete(-1)
//        }
//
//        assertAll(
//            { assertTrue(result.message.contains("No existe el servicio")) },
//        )
//        coVerify { service.deleteService(any()) }
//    }
//
//    @Test
//    fun update() = runTest {
//        coEvery { service.findServiceById(any()) } returns serviceTest
//        coEvery { service.updateService(any(), any()) } returns serviceTest
//
//        val result = controller.update(1, serviceTest)
//        val res = result.body!!
//
//        assertAll(
//            { assertEquals(result.statusCode, HttpStatus.OK) },
//            { assertEquals(res.price, serviceTest.price) },
//            { assertEquals(serviceTest.category, res.category) },
//            { assertEquals(serviceTest.url, res.url) },
//            { assertEquals(serviceTest.available, res.available) }
//        )
//        coVerify { service.findServiceById(any()) }
//        coVerify { service.updateService(any(), any()) }
//    }
//
//    @Test
//    fun updateNotFound() = runTest {
//        coEvery { service.findServiceById(any()) } returns null
//
//        val result = controller.update(1, serviceTest)
//
//        assertAll(
//            { assertEquals(result.statusCode, HttpStatus.NOT_FOUND) },
//        )
//        coVerify { service.findServiceById(any()) }
//    }
//}