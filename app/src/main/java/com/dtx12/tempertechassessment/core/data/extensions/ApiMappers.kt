package com.dtx12.tempertechassessment.core.data.extensions

import com.dtx12.tempertechassessment.core.data.models.*
import com.dtx12.tempertechassessment.core.domain.models.*
import com.dtx12.tempertechassessment.core.extensions.applySystemZone

private fun ApiMoney.toDomain(): Money {
    return Money(currency = currency, amount = amount)
}

private fun ApiCategoryTranslation.toDomain(): CategoryTranslation {
    return CategoryTranslation(enGB = enGB, nLNL = nlNL)
}

private fun ApiCategory.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        nameTranslation = nameTranslation.toDomain()
    )
}

private fun ApiCountry.toDomain(): Country {
    return Country(human = human)
}

private fun ApiGeo.toDomain(): Geo {
    return Geo(lat = lat, lon = lon)
}

private fun ApiAddress.toDomain(): Address {
    return Address(
        zipCode = zipCode,
        street = street,
        number = number,
        numberWithExtra = numberWithExtra,
        extra = extra,
        city = city,
        line1 = line1,
        line2 = line2,
        country = country.toDomain(),
        geo = geo.toDomain(),
        region = region
    )
}

private fun ApiClientLinks.toDomain(): ClientLinks {
    return ClientLinks(heroImage = heroImage)
}

private fun ApiClient.toDomain(): Client {
    return Client(id = id, name = name, links = links?.toDomain())
}

private fun ApiProject.toDomain(): Project {
    return Project(id = id, client = client.toDomain())
}

private fun ApiJob.toDomain(): Job {
    return Job(
        id = id,
        title = title,
        project = project.toDomain(),
        category = category.toDomain(),
        reportAtAddress = reportAtAddress.toDomain()
    )
}

fun ApiShiftData.toDomain(): Shift {
    return Shift(
        id = id,
        earliestPossibleStartTime = earliestPossibleStartTime.applySystemZone(),
        latestPossibleEndTime = latestPossibleEndTime.applySystemZone(),
        averageEstimatedEarningPerHour = averageEstimatedEarningPerHour.toDomain(),
        job = job.toDomain()
    )
}